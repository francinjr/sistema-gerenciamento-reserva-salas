package com.francinjr.sistema_gerenciamento_reserva_salas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SistemaGerenciamentoReservaSalasApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SistemaGerenciamentoReservaSalasApplication.class, args);
    }
}
