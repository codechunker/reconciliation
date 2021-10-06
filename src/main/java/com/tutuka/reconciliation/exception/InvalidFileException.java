package com.tutuka.reconciliation.exception;

import lombok.Data;

import java.util.List;

@Data
public class InvalidFileException extends RuntimeException {
    private List<String> errorMessages;
    private String code;


    public InvalidFileException(String message, String code, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
        this.code = code;
    }


}
