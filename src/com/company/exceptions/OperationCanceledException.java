package com.company.exceptions;

public class OperationCanceledException extends Exception{
    public OperationCanceledException() {
        super("Operation was canceled");
    }
}
