package com.cwd.tg.ugc.errors.exceptions;

public class UnauthorizedTokenException extends RuntimeException {

    public UnauthorizedTokenException(String message) {
        super(message);
    }
}
