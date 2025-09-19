package com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.ClienteRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.domain.services.AgendamentoService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.DetalhesAgendamentoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.dtos.SalvarAgendamentoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.agendamento.web.mappers.AgendamentoMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services.SalaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SalaService salaService;
    private final ClienteRepository clienteRepository;
    private final AgendamentoMapper agendamentoMapper;

    @GetMapping("/solicitar/{salaId}")
    public String exibirFormularioSolicitacao(@PathVariable Long salaId, Model model) {
        Sala sala = salaService.buscarPorId(salaId);
        model.addAttribute("sala", sala);
        SalvarAgendamentoDto dto = new SalvarAgendamentoDto();
        dto.setSalaId(salaId);
        model.addAttribute("agendamentoDto", dto);
        return "agendamentos/formulario-cliente";
    }

    @PostMapping("/solicitar")
    public String processarSolicitacao(
            @Valid @ModelAttribute("agendamentoDto") SalvarAgendamentoDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            Sala sala = salaService.buscarPorId(dto.getSalaId());
            model.addAttribute("sala", sala);
            return "agendamentos/formulario-cliente";
        }

        try {
            Cliente cliente = clienteRepository.findByUsuarioId(usuarioLogado.getId())
                    .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));
            agendamentoService.solicitar(dto, cliente);
        } catch (DominioException e) {
            bindingResult.rejectValue(null, "erro.global", e.getMessage());
            Sala sala = salaService.buscarPorId(dto.getSalaId());
            model.addAttribute("sala", sala);
            return "agendamentos/formulario-cliente";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Solicitação de agendamento enviada com sucesso!");
        return "redirect:/";
    }

    @PostMapping("/cancelar/{agendamentoId}")
    public String cancelarSolicitacao(@PathVariable Long agendamentoId,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes) {

        Cliente cliente = clienteRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil de cliente não encontrado para o usuário logado."));

        try {
            agendamentoService.cancelarSolicitacao(agendamentoId, cliente);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Sua solicitação de agendamento foi cancelada com sucesso.");
        } catch (DominioException | SecurityException e) {
            redirectAttributes.addFlashAttribute("erros",
                    Collections.singletonList(e.getMessage()));
        }

        return "redirect:/";
    }

    @GetMapping("/solicitacao/{agendamentoId}")
    public String exibirDetalhesSolicitacao(@PathVariable Long agendamentoId,
            @AuthenticationPrincipal Usuario usuarioLogado,
            Model model) {

        Cliente cliente = clienteRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalStateException("Perfil de cliente não encontrado."));

        Agendamento agendamento = agendamentoService.buscarSolicitacaoDoCliente(agendamentoId,
                cliente);
        DetalhesAgendamentoDto dto = agendamentoMapper.paraDetalhesDto(agendamento);

        model.addAttribute("agendamento", dto);

        return "agendamentos/detalhes-solicitacao";
    }
}