package com.pocketpipo.pocketpipo.exception;

public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(String msg) {
        super(msg);
    }
}
