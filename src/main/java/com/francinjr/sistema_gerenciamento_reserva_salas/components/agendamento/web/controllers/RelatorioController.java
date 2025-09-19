package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.services.RelatorioService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.FiltroRelatorioDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.ClienteRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.repositories.RecepcionistaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {
    private final RelatorioService relatorioService;
    private final ClienteRepository clienteRepository;
    private final RecepcionistaRepository recepcionistaRepository;

    @GetMapping("/agendamentos")
    public String relatorioAgendamentos(@ModelAttribute FiltroRelatorioDto filtro,
            @AuthenticationPrincipal Usuario usuarioLogado,
            Model model) {

        Cliente cliente = null;
        Recepcionista recepcionista = null;

        // Determina o tipo de filtro a ser aplicado com base no papel do usuário
        if (usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            cliente = clienteRepository.findByUsuarioId(usuarioLogado.getId()).orElse(null);
        } else if (usuarioLogado.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECEPCIONISTA"))) {
            recepcionista = recepcionistaRepository.findByUsuarioId(usuarioLogado.getId()).orElse(null);
        }

        var relatorio = relatorioService.gerarRelatorio(filtro, cliente, recepcionista != null ? recepcionista.getSetor() : null);

        model.addAttribute("filtro", filtro);
        model.addAttribute("relatorio", relatorio);
        return "relatorios/agendamentos";
    }
}
