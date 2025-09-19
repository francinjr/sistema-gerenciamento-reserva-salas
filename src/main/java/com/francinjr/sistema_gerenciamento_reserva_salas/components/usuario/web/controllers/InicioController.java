package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inicio")
public class InicioController {

    /**
     * Exibe a página de boas-vindas principal da aplicação após o login.
     */
    @GetMapping
    public String paginaDeInicio() {
        // Aponta para o novo arquivo JSP que vamos criar
        return "inicio/index";
    }
}
