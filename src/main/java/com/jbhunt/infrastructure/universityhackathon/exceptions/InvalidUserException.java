package com.jbhunt.infrastructure.universityhackathon.exceptions;

public class InvalidUserException extends Exception{
    public InvalidUserException(String message){
        super(message);
    }
}
