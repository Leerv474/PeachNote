package io.leerv.peach_note.auth.email;

import io.leerv.peach_note.activationToken.ActivationToken;
import io.leerv.peach_note.activationToken.ActivationTokenRepository;
import io.leerv.peach_note.activationToken.ActivationTokenService;
import io.leerv.peach_note.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final ActivationTokenService tokenService;
    @Value("${application.mailing.sender-email}")
    private String senderEmail;
    @Value("${application.mailing.activation-url}")
    private String activationUrl;

    public void sendValidationEmail(User user) throws MessagingException {
        String token = tokenService.generateActivationToken();
        String url = activationUrl + tokenService.generateActivationUrl();
        tokenService.saveActivationToken(token, url, user);
        sendEmail(user.getEmail(), user.getUsername(), EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl, token, "Account activation");
    }

    @Async
    private void sendEmail(String receiver,
                           String username,
                           EmailTemplateName emailTemplate,
                           String token,
                           String url,
                           String subject
    ) throws MessagingException {
        String templateName = emailTemplate == null ? "confirm-email" : emailTemplate.name();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", url);
        properties.put("activationCode", token);

        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setFrom(senderEmail);
        String template = templateEngine.process(templateName, context);
        mimeMessageHelper.setText(template, true);
        mailSender.send(mimeMessage);
    }

}