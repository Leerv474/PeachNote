package io.leerv.peach_note.exceptions;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionDto> handleException(LockedException exception) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(
                        ExceptionDto.builder()
                                .businessCode(BusinessCodes.ACCOUNT_LOCKED.getCode())
                                .businessErrorDescription(BusinessCodes.ACCOUNT_LOCKED.getDescription())
                                .build()
                );

    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .businessCode(BusinessCodes.BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BusinessCodes.BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> handleException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .businessCode(BusinessCodes.BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BusinessCodes.BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionDto> handleException(MessagingException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDto.builder()
                        .error(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionDto> handleException(ServletException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionDto.builder()
                                .businessCode(BusinessCodes.NO_CODE.getCode())
                                .businessErrorDescription(BusinessCodes.NO_CODE.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handleException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionDto.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(RecordNotFound.class)
    public ResponseEntity<ExceptionDto> handleException(RecordNotFound exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(ExpirationException.class)
    public ResponseEntity<ExceptionDto> handleException(ExpirationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionDto> handleException(OperationNotPermittedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ExceptionDto.builder()
                            .error(exception.getMessage())
                            .build()
                );
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionDto.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(IllegalRequestContentException.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalRequestContentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
}
