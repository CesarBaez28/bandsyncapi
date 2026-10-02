package com.bandsyncapi.bandsyncapi.exceptions;

/**
 * Custom exception to handle errors when saving files
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
