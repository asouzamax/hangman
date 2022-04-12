package com.dell.hangman.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidGuessException extends BusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidGuessException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}