package com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
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
            throw new DominioException("O valor não pode ser nulo.");
        }

        BigDecimal valorConvertido = BigDecimal.valueOf(valor);

        if (valorConvertido.scale() > ESCALA_PADRAO) {
            throw new IllegalArgumentException(
                    "O valor de entrada não pode ter mais de " + ESCALA_PADRAO + " casas decimais."
            );
        }

        this.valor = valorConvertido.setScale(ESCALA_PADRAO, MODO_ARREDONDAMENTO_PADRAO);
    }

    public Dinheiro(BigDecimal valor) {
        if (valor == null) {
            throw new DominioException("O valor monetário não pode ser nulo.");
        }
        this.valor = valor.setScale(ESCALA_PADRAO, MODO_ARREDONDAMENTO_PADRAO);
    }

    /**
     * Soma o valor deste objeto Dinheiro com outro, retornando um novo objeto Dinheiro.
     */
    public Dinheiro somar(Dinheiro outro) {
        if (outro == null) {
            return this;
        }
        return new Dinheiro(this.valor.add(outro.getValor()));
    }

    /**
     * Subtrai o valor de outro objeto Dinheiro deste, retornando um novo objeto Dinheiro.
     */
    public Dinheiro subtrair(Dinheiro outro) {
        if (outro == null) {
            return this;
        }
        return new Dinheiro(this.valor.subtract(outro.getValor()));
    }

    /**
     * Multiplica o valor deste objeto Dinheiro por um fator (ex: 0.5 para 50%).
     */
    public Dinheiro multiplicar(double fator) {
        BigDecimal resultado = this.valor.multiply(BigDecimal.valueOf(fator));
        return new Dinheiro(resultado);
    }
}
