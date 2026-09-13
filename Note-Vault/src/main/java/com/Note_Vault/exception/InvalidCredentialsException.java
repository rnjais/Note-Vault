package com.Note_Vault.exception;

public class InvalidCredentialsException extends RuntimeException{

    public  InvalidCredentialsException(String message){
        super(message);
    }
}
