package com.francinjr.sistema_gerenciamento_reserva_salas.components.sala.domain.valueobjects;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.DomainException;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class Dinheiro {

    private static final int ESCALA_PADRAO = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO_PADRAO = RoundingMode.HALF_UP;
    private BigDecimal valor;

    public Dinheiro(Double valor) {
        if (valor == null) {
            throw new DomainException("O valor não pode ser nulo.");
        }

        BigDecimal valorConvertido = BigDecimal.valueOf(valor);

        if (valorConvertido.scale() > ESCALA_PADRAO) {
            throw new IllegalArgumentException(
                    "O valor de entrada não pode ter mais de " + ESCALA_PADRAO + " casas decimais."
            );
        }

        this.valor = valorConvertido.setScale(ESCALA_PADRAO, MODO_ARREDONDAMENTO_PADRAO);
    }
}
