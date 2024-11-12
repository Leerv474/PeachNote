package io.leerv.peach_note.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessCodes {
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "NOT IMPLEMENTED"),
    INCORRECT_CURRENT_PASSWORD(400, HttpStatus.BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(400, HttpStatus.BAD_REQUEST, "New password does not match"),
    ACCOUNT_LOCKED(423, HttpStatus.LOCKED, "User account is locked"),
    ACCOUNT_DISABLED(403, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(404, HttpStatus.FORBIDDEN, "Login and / or password is incorrect")
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
