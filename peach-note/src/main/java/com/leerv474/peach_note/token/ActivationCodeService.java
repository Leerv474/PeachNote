package com.leerv474.peach_note.token;

import com.leerv474.peach_note.auth.email.EmailService;
import com.leerv474.peach_note.auth.email.EmailTemplateName;
import com.leerv474.peach_note.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationCodeService {
    @Autowired
    private final ActivationCodeRepository activationCodeRepository;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.mailing.backend.activation-length}")
    private int activationLength;
    private final EmailService emailService;

    public void sendValidationEmail(User user) throws MessagingException {
        String activationCode = generateActivationCode(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                activationCode,
                "Account activation"
        );
    }

    private String generateActivationCode(User user) {
        String activationCode = buildActivationCode();
        ActivationCode token = ActivationCode.builder()
                .code(activationCode)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .owner(user)
                .build();
        activationCodeRepository.save(token);
        return activationCode;
    }

    private String buildActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < this.activationLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

}

