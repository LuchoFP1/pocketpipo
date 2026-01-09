package com.pocketpipo.pocketpipo.exception;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException (String msg) { 
        super(msg); }
}
