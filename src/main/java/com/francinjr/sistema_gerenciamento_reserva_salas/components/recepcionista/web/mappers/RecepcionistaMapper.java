package com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.SalvarPessoaFisicaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.mappers.PessoaFisicaMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.domain.entities.Recepcionista;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos.BuscarRecepcionistaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.recepcionista.web.dtos.SalvarRecepcionistaDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.setor.web.mappers.SetorMapper;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.SalvarUsuarioDto;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.mappers.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecepcionistaMapper {

    private final SetorMapper setorMapper;
    private final PessoaFisicaMapper pessoaFisicaMapper;
    private final UsuarioMapper usuarioMapper;

    public BuscarRecepcionistaDto paraDto(Recepcionista recepcionista) {
        if (recepcionista == null) {
            return null;
        }
        return new BuscarRecepcionistaDto(
                recepcionista.getId(),
                setorMapper.paraDto(recepcionista.getSetor()),
                pessoaFisicaMapper.paraDto(recepcionista.getPessoaFisica()),
                usuarioMapper.paraDto(recepcionista.getUsuario())
        );
    }

    public SalvarRecepcionistaDto paraSalvarDto(Recepcionista recepcionista) {
        if (recepcionista == null) {
            return null;
        }

        SalvarPessoaFisicaDto pessoaDto = new SalvarPessoaFisicaDto(
                recepcionista.getPessoaFisica().getNome(),
                recepcionista.getPessoaFisica().getCpf().getValor(),
                recepcionista.getPessoaFisica().getTelefone()
        );
        SalvarUsuarioDto usuarioDto = new SalvarUsuarioDto(
                recepcionista.getUsuario().getEmail().getValor(),
                ""
        );

        return new SalvarRecepcionistaDto(
                pessoaDto,
                usuarioDto,
                recepcionista.getSetor().getId()
        );
    }

    public Page<BuscarRecepcionistaDto> paginaEntidadeParaPaginaDto(Page<Recepcionista> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }
}