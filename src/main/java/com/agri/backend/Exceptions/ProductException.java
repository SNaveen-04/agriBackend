package com.agri.backend.Exceptions;

import org.springframework.http.HttpStatus;

public class ProductException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final HttpStatus status;

    public ProductException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}