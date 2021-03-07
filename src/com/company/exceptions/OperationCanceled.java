package com.company.exceptions;

public class OperationCanceled extends Exception{
    public OperationCanceled() {
        super("Operation was canceled");
    }
}
