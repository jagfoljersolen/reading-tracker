package com.example.readingtracker.book.exception;

public class InvalidBookStateException extends RuntimeException{
    public InvalidBookStateException(String message){
        super(message);
    }
}
