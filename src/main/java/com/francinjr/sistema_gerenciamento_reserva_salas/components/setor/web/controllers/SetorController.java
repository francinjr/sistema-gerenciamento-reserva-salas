package com.francinjr.sistema_gerenciamento_reserva_salas.presentation.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.NegocioException;
import com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils.DataIntegrityViolationTranslator;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.SalvarSetorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/setores")
public class SetorController {

    private final SetorService setorService;
    private final DataIntegrityViolationTranslator violationTranslator;

    @GetMapping("/listar")
    public String listarSetores(Model model) {
        //model.addAttribute("setores", setorService.buscarTodos());
        return "setores/listar";
    }

    @GetMapping("/novo")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("setorDto", new SalvarSetorDto());
        return "setores/formulario";
    }

    @PostMapping("/salvar")
    public String processarFormularioCriacao(
            @Valid @ModelAttribute("setorDto") SalvarSetorDto setorDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "setores/formulario";
        }

        try {
            setorService.criar(setorDto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            return "setores/formulario";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Setor criado com sucesso!");
        return "redirect:/setores/listar";
    }
}