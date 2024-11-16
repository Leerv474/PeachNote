package io.leerv.peach_note.exceptions;

public class IllegalRequestContentException extends RuntimeException {
    public IllegalRequestContentException(String message) {
        super(message);
    }
}
