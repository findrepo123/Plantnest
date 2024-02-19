package com.project.api.exceptions.domain;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }
}
