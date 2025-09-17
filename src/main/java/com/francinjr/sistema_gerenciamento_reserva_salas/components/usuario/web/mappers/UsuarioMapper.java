package com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.mappers;

import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.domain.entities.Usuario;
import com.francinjr.sistema_gerenciamento_reserva_salas.components.usuario.web.dtos.BuscarUsuarioDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public BuscarUsuarioDto paraDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new BuscarUsuarioDto(usuario.getId(), usuario.getEmail().getValor(),
                usuario.getPapel());
    }

    public Page<BuscarUsuarioDto> paginaEntidadeParaPaginaDto(Page<Usuario> page) {
        if (page == null) {
            return null;
        }
        return page.map(this::paraDto);
    }
}
