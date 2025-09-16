package com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils.DataIntegrityViolationTranslator;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.entities.Setor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.BuscarSetorDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.dtos.SalvarSetorDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.mappers.SetorMapper;
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
@RequestMapping("/setores")
public class SetorController {

    private final SetorService setorService;
    private final DataIntegrityViolationTranslator violationTranslator;
    private final SetorMapper setorMapper;

    @GetMapping("/listar")
    public String listarSetores(
            @RequestParam(name = "nome", required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            Model model) {

        Page<Setor> setoresPageDeEntidades = setorService.buscar(nome, pageable);
        Page<BuscarSetorDto> setoresPageDeDtos = setorMapper.paginaEntidadeParaPaginaDto(setoresPageDeEntidades);

        model.addAttribute("setoresPage", setoresPageDeDtos);
        model.addAttribute("termoBusca", nome);

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

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Setor setor = setorService.buscarPorId(id);
        SalvarSetorDto setorDto = new SalvarSetorDto(setor.getNome(), setor.getDescricao());

        model.addAttribute("setorDto", setorDto);
        model.addAttribute("setorId", id);

        return "setores/formulario";
    }

    @PostMapping("/atualizar/{id}")
    public String processarFormularioEdicao(@PathVariable Long id,
            @Valid @ModelAttribute("setorDto") SalvarSetorDto setorDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("setorId", id);
            return "setores/formulario";
        }

        try {
            setorService.atualizar(id, setorDto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            redirectAttributes.addAttribute("setorId", id);
            return "setores/formulario";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Setor atualizado com sucesso!");
        return "redirect:/setores/listar";
    }


    @PostMapping("/excluir/{id}")
    public String excluirSetor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        setorService.excluir(id);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Setor excluído com sucesso.");
        return "redirect:/setores/listar";
    }
}