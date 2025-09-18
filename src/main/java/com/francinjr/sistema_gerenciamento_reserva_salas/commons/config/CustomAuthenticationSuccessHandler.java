package com.francinjr.sistema_gerenciamento_reserva_salas.commons.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // O Spring Security adiciona o prefixo "ROLE_" automaticamente.
        // Verificamos da role mais alta para a mais baixa.
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            return "/setores/listar"; // Admin vai para o CRUD de setores
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECEPCIONISTA"))) {
            return "/agendamentos/solicitacoes"; // Recepcionista vai para a lista de solicitações
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            return "/"; // Cliente vai para o dashboard de visualização de salas
        } else {
            throw new IllegalStateException("Usuário com papel desconhecido.");
        }
    }
}
