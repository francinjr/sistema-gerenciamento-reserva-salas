package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.ClienteRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services.DashboardService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SetorComSalasDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ClienteRepository clienteRepository;

    @GetMapping("/")
    public String dashboard(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestParam(name = "nomeSetor", required = false) String nomeSetor,
            Model model) {

        Cliente cliente = clienteRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil de cliente não encontrado para o usuário logado."));

        List<SetorComSalasDto> setores = dashboardService.getDadosDashboardCliente(cliente,
                nomeSetor);

        model.addAttribute("setores", setores);
        model.addAttribute("termoBuscaSetor", nomeSetor);

        return "dashboard/index-cliente";
    }
}
