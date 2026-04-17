package com.example.sr.exception;

public class ErrorMessages {

    private ErrorMessages() {
        throw new IllegalStateException("Utility class");
    }

    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String WRONG_PASSWORD = "Wrong Password";
}
