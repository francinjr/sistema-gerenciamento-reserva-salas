package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils.DataIntegrityViolationTradutor;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.services.RecepcionistaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos.BuscarRecepcionistaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos.SalvarRecepcionistaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.mappers.RecepcionistaMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.domain.services.SetorService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;
    private final SetorService setorService;
    private final RecepcionistaMapper recepcionistaMapper;
    private final DataIntegrityViolationTradutor violationTranslator;

    @GetMapping("/listar")
    public String listarRecepcionistas(
            @RequestParam(name = "nome", required = false) String nome,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Model model) {

        Page<Recepcionista> recepcionistasPage = recepcionistaService.buscar(nome, pageable);
        Page<BuscarRecepcionistaDto> dtosPage = recepcionistaMapper.paginaEntidadeParaPaginaDto(recepcionistasPage);

        model.addAttribute("recepcionistasPage", dtosPage);
        model.addAttribute("termoBusca", nome);

        return "recepcionistas/listar";
    }

    @GetMapping("/novo")
    public String exibirFormularioCriacao(Model model) {
        SalvarRecepcionistaDto dto = new SalvarRecepcionistaDto();
        dto.setPessoaFisica(new SalvarPessoaFisicaDto());
        dto.setUsuario(new SalvarUsuarioDto());

        model.addAttribute("recepcionistaDto", dto);
        model.addAttribute("setores", setorService.buscarTodos());
        return "recepcionistas/formulario";
    }

    @PostMapping("/salvar")
    public String processarFormularioCriacao(
            @Valid @ModelAttribute("recepcionistaDto") SalvarRecepcionistaDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // ========== DEBUG: VERIFICAR OS ERROS DE VALIDAÇÃO ==========
        if (bindingResult.hasErrors()) {
            System.out.println("--- ERROS DE VALIDAÇÃO ENCONTRADOS ---");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            System.out.println("------------------------------------");

            model.addAttribute("setores", setorService.buscarTodos());
            return "recepcionistas/formulario";
        }
        // ======================= FIM DO DEBUG ========================

        try {
            recepcionistaService.criar(dto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            model.addAttribute("setores", setorService.buscarTodos());
            return "recepcionistas/formulario";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Recepcionista criado com sucesso!");
        return "redirect:/recepcionistas/listar";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Recepcionista recepcionista = recepcionistaService.buscarPorId(id);
        SalvarRecepcionistaDto dto = recepcionistaMapper.paraSalvarDto(recepcionista);

        model.addAttribute("recepcionistaDto", dto);
        model.addAttribute("recepcionistaId", id);
        model.addAttribute("setores", setorService.buscarTodos());
        return "recepcionistas/formulario";
    }

    @PostMapping("/atualizar/{id}")
    public String processarFormularioEdicao(@PathVariable Long id,
            @Valid @ModelAttribute("recepcionistaDto") SalvarRecepcionistaDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("recepcionistaId", id);
            model.addAttribute("setores", setorService.buscarTodos());
            return "recepcionistas/formulario";
        }
        try {
            recepcionistaService.atualizar(id, dto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            model.addAttribute("recepcionistaId", id);
            model.addAttribute("setores", setorService.buscarTodos());
            return "recepcionistas/formulario";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Recepcionista atualizado com sucesso!");
        return "redirect:/recepcionistas/listar";
    }

    @PostMapping("/excluir/{id}")
    public String excluirRecepcionista(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        recepcionistaService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Recepcionista excluído com sucesso.");
        return "redirect:/recepcionistas/listar";
    }
}
