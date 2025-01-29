package com.example.demo_api_rest.exception;

public class UserNameUniqueViolationException extends RuntimeException{
    public UserNameUniqueViolationException(String message) {
        super(message);
    }
}
