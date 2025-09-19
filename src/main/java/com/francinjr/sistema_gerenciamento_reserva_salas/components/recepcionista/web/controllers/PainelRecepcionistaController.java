package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.controllers;


import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services.AgendamentoService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.mappers.AgendamentoMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.repositories.RecepcionistaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/painel-recepcionista")
@RequiredArgsConstructor
public class PainelRecepcionistaController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoMapper agendamentoMapper;
    private final RecepcionistaRepository recepcionistaRepository;

    @GetMapping("/dashboard")
    public String dashboardRecepcionista(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @PageableDefault(size = 10, sort = "dataHoraInicio") Pageable pageable,
            Model model) {

        Recepcionista recepcionista = recepcionistaRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil de recepcionista não encontrado para o usuário logado."));

        // Lógica unificada: Busca todos os agendamentos ativos do setor
        Page<Agendamento> agendamentosPage = agendamentoService.buscarAgendamentosAtivosPorSetor(
                recepcionista.getSetor(), pageable);

        model.addAttribute("agendamentosPage", agendamentosPage.map(agendamentoMapper::paraDto));

        return "painel-recepcionista/dashboard";
    }

    @PostMapping("/solicitacoes/{id}/confirmar")
    public String confirmarAgendamento(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.confirmar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Agendamento confirmado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erros",
                    Collections.singletonList("Erro ao confirmar agendamento: " + e.getMessage()));
        }
        return "redirect:/painel-recepcionista/dashboard";
    }

    @PostMapping("/solicitacoes/{id}/recusar")
    public String recusarAgendamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.recusar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Solicitação recusada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erros",
                    Collections.singletonList(e.getMessage()));
        }
        return "redirect:/painel-recepcionista/dashboard";
    }

    @PostMapping("/agendamentos/{id}/finalizar")
    public String finalizarAgendamento(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.finalizar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Agendamento finalizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erros",
                    List.of("Erro ao finalizar agendamento: " + e.getMessage()));
        }
        return "redirect:/painel-recepcionista/dashboard";
    }
}