package com.tutuka.reconciliation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse<T> {
    private static final long serialVersionUID = 1L;
    private String status;
    private String statusMessage;
    private T data;

    public GenericResponse(String status, String statusMessage, T data) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.data = data;
    }
}
