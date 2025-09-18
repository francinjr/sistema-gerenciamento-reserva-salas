package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.controllers;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils.DataIntegrityViolationTranslator;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services.ClienteService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalvarClienteDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
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

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final DataIntegrityViolationTranslator violationTranslator;

    @GetMapping("/novo")
    public String exibirFormularioDeCadastro(Model model) {
        SalvarClienteDto dto = new SalvarClienteDto();
        dto.setPessoaFisica(new SalvarPessoaFisicaDto());
        dto.setUsuario(new SalvarUsuarioDto());
        model.addAttribute("clienteDto", dto);
        return "autenticacao/cadastro";
    }

    @PostMapping
    public String processarCadastroDeCliente(
            @Valid @ModelAttribute("clienteDto") SalvarClienteDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "autenticacao/cadastro";
        }
        try {
            clienteService.registrarNovoCliente(dto);
        } catch (DataIntegrityViolationException e) {
            violationTranslator.translate(e, bindingResult);
            return "autenticacao/cadastro";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Cadastro realizado com sucesso! Faça o login.");
        return "redirect:/login";
    }
}
