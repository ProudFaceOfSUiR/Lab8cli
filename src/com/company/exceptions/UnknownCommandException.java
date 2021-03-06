package com.company.exceptions;

public class UnknownCommandException extends Exception{
    public UnknownCommandException(){
        super("Unknown command. Operation cancelled");
    }

    public UnknownCommandException(String message) {
        super(message);
    }
}
