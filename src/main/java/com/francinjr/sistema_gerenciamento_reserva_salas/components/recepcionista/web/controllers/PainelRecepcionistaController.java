package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Agendamento;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services.AgendamentoService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services.ClienteService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.AgendamentoInstantaneoDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.mappers.AgendamentoMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.repositories.RecepcionistaRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services.SalaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.BuscarSalaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.mappers.SalaMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/painel-recepcionista")
@RequiredArgsConstructor
public class PainelRecepcionistaController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoMapper agendamentoMapper;
    private final RecepcionistaRepository recepcionistaRepository;
    private final SalaService salaService;
    private final SalaMapper salaMapper;
    private final ClienteService clienteService;

    /**
     * Exibe o painel principal da recepcionista com todos os agendamentos ativos (solicitados e
     * confirmados).
     */
    @GetMapping("/dashboard")
    public String dashboardRecepcionista(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @PageableDefault(size = 10, sort = "dataHoraInicio") Pageable pageable,
            Model model) {

        Recepcionista recepcionista = recepcionistaRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Perfil de recepcionista não encontrado para o usuário logado."));

        Page<Agendamento> agendamentosPage = agendamentoService.buscarAgendamentosAtivosPorSetor(
                recepcionista.getSetor(), pageable);

        model.addAttribute("agendamentosPage", agendamentosPage.map(agendamentoMapper::paraDto));

        return "painel-recepcionista/dashboard";
    }

    /**
     * Processa a confirmação de uma solicitação de agendamento.
     */
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

    /**
     * Processa a recusa de uma solicitação de agendamento.
     */
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

    /**
     * Processa a finalização de um agendamento confirmado.
     */
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

    /**
     * Exibe a página com a lista de salas do setor da recepcionista para agendamento instantâneo.
     */
    @GetMapping("/agendar-sala")
    public String exibirSalasParaAgendamento(@AuthenticationPrincipal Usuario usuarioLogado,
            Model model) {
        Recepcionista recepcionista = recepcionistaRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(
                        () -> new IllegalStateException("Perfil de recepcionista não encontrado."));

        List<Sala> salasDoSetor = salaService.buscarPorSetor(recepcionista.getSetor());
        List<BuscarSalaDto> salasDto = salaMapper.paraListaDto(salasDoSetor);

        model.addAttribute("salas", salasDto);
        model.addAttribute("setor", recepcionista.getSetor());

        return "painel-recepcionista/agendar-sala";
    }

    /**
     * Exibe o formulário para agendamento instantâneo de uma sala específica.
     */
    @GetMapping("/agendamentos/novo/{salaId}")
    public String exibirFormularioAgendamentoInstantaneo(@PathVariable Long salaId, Model model) {
        AgendamentoInstantaneoDto dto = new AgendamentoInstantaneoDto();
        dto.setSalaId(salaId);

        model.addAttribute("agendamentoDto", dto);
        model.addAttribute("sala", salaService.buscarPorId(salaId));
        model.addAttribute("clientes", clienteService.buscarTodos());

        return "painel-recepcionista/formulario-agendamento";
    }

    /**
     * Processa a submissão do formulário de agendamento instantâneo.
     */
    @PostMapping("/agendamentos/salvar")
    public String processarAgendamentoInstantaneo(
            @Valid @ModelAttribute("agendamentoDto") AgendamentoInstantaneoDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("sala", salaService.buscarPorId(dto.getSalaId()));
            model.addAttribute("clientes", clienteService.buscarTodos());
            return "painel-recepcionista/formulario-agendamento";
        }

        try {
            agendamentoService.agendarInstantaneamente(dto);
        } catch (Exception e) {
            bindingResult.rejectValue(null, "erro.global", "Erro ao agendar: " + e.getMessage());
            model.addAttribute("sala", salaService.buscarPorId(dto.getSalaId()));
            model.addAttribute("clientes", clienteService.buscarTodos());
            return "painel-recepcionista/formulario-agendamento";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Agendamento realizado com sucesso!");
        return "redirect:/painel-recepcionista/dashboard";
    }
}