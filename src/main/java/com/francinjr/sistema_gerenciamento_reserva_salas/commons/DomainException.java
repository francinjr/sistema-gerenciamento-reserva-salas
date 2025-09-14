package com.francinjr.sistema_gerenciamento_reserva_salas.commons;

public class DomainException extends RuntimeException {

    public DomainException(String mensagem) {
        super(mensagem);
    }
}
