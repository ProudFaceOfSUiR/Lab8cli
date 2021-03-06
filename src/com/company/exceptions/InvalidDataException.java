package com.company.exceptions;

public class InvalidDataException extends Exception{
    public InvalidDataException(String invalidData) {
        super("Invalid" + invalidData);
    }

    public InvalidDataException(String invalidData, String message) {
        super("Invalid " + invalidData + ". " + message);
    }
}
