package com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.services;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.entities.Cliente;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.domain.repositories.ClienteRepository;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.cliente.web.dtos.SalvarClienteDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities.PessoaFisica;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.services.PessoaFisicaService;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.PapelUsuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PessoaFisicaService pessoaFisicaService;
    private final UsuarioService usuarioService;

    @Transactional
    public Cliente registrarNovoCliente(SalvarClienteDto dto) {
        PessoaFisica pessoaFisica = pessoaFisicaService.criar(dto.getPessoaFisica());
        Usuario usuario = usuarioService.criar(dto.getUsuario(), PapelUsuario.CLIENTE);

        Cliente novoCliente = new Cliente(pessoaFisica, usuario);
        return clienteRepository.save(novoCliente);
    }
}
