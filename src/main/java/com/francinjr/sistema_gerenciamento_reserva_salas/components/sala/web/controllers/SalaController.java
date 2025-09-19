package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils.DataIntegrityViolationTradutor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.entities.Sala;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.services.SalaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.BuscarSalaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.dtos.SalvarSalaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.web.mappers.SalaMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;
    private final SetorService setorService;
    private final SalaMapper salaMapper;
    private final DataIntegrityViolationTradutor violationTranslator;

    @GetMapping("/listar")
    public String listarSalas(
            @RequestParam(name = "nome", required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            Model model) {

        Page<Sala> salasPage = salaService.buscar(nome, pageable);
        Page<BuscarSalaDto> dtosPage = salaMapper.paginaEntidadeParaPaginaDto(salasPage);

        model.addAttribute("salasPage", dtosPage);
        model.addAttribute("termoBusca", nome);

        return "salas/listar";
    }

    @GetMapping("/novo")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("salaDto", new SalvarSalaDto());
        model.addAttribute("setores", setorService.buscarTodos());

        return "salas/formulario";
    }

    @PostMapping("/salvar")
    public String processarFormularioCriacao(
            @Valid @ModelAttribute("salaDto") SalvarSalaDto salaDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("setores", setorService.buscarTodos());
            return "salas/formulario";
        }
        try {
            salaService.criar(salaDto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            model.addAttribute("setores", setorService.buscarTodos());
            return "salas/formulario";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Sala criada com sucesso!");
        return "redirect:/salas/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Sala sala = salaService.buscarPorId(id);
        SalvarSalaDto salaDto = new SalvarSalaDto(
                sala.getNome(),
                sala.getPreco().getValor().doubleValue(),
                sala.getDescricao(),
                sala.getSetor().getId(),
                sala.getCapacidadeMaxima()
        );

        model.addAttribute("salaDto", salaDto);
        model.addAttribute("salaId", id);
        model.addAttribute("setores",
                setorService.buscarTodos()); // Lista de setores para o <select>

        return "salas/formulario";
    }

    @PostMapping("/atualizar/{id}")
    public String processarFormularioEdicao(@PathVariable Long id,
            @Valid @ModelAttribute("salaDto") SalvarSalaDto salaDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("salaId", id);
            model.addAttribute("setores", setorService.buscarTodos());
            return "salas/formulario";
        }
        try {
            salaService.atualizar(id, salaDto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            model.addAttribute("salaId", id);
            model.addAttribute("setores", setorService.buscarTodos());
            return "salas/formulario";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Sala atualizada com sucesso!");
        return "redirect:/salas/listar";
    }

    @PostMapping("/excluir/{id}")
    public String excluirSala(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        salaService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Sala excluída com sucesso.");
        return "redirect:/salas/listar";
    }
}