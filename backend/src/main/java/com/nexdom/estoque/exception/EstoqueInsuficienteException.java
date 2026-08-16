package com.nexdom.estoque.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}