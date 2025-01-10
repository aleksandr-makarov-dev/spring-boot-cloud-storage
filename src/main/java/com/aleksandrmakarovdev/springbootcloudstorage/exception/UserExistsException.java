package com.aleksandrmakarovdev.springbootcloudstorage.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
