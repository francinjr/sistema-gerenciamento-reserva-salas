package com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.domain.entities.PessoaFisica;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.pessoa.web.dtos.BuscarPessoaFisicaDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PessoaFisicaMapper {

    public BuscarPessoaFisicaDto paraDto(PessoaFisica pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new BuscarPessoaFisicaDto(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf().getValor(),
                pessoa.getTelefone()
        );
    }

    public Page<BuscarPessoaFisicaDto> paginaEntidadeParaPaginaDto(Page<PessoaFisica> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }
}
