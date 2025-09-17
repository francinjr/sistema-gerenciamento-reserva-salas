package com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.entities.CampoValidacao;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Service
public class CamposUnicosEmUsoException extends RuntimeException {

    private List<CampoValidacao> valoresCamposExistentes;

    public CamposUnicosEmUsoException(
            String mensagem, List<CampoValidacao> valoresCamposExistentes) {
        super(mensagem);
        this.valoresCamposExistentes = valoresCamposExistentes;
    }
}
