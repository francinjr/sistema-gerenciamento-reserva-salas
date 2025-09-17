package com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class Email {

    private String valor;

    private static final int TAMANHO_MINIMO = 6;
    private static final int TAMANHO_MAXIMO = 254;
    private static final Pattern PADRAO_EMAIL = Pattern.compile(
            "^[\\p{L}0-9_+&*-]+(?:\\.[\\p{L}0-9_+&*-]+)*@" +
                    "(?:[\\p{L}0-9-]+\\.)+[\\p{L}]{2,}$"
    );

    public Email(String valorBruto) {
        if (valorBruto == null) {
            throw new DominioException("O e-mail não pode ser nulo.");
        }

        String valorProcessado = processar(valorBruto);
        validar(valorProcessado);
        this.valor = valorProcessado;
    }

    private String processar(String valorBruto) {
        return valorBruto.trim();
    }

    private void validar(String valorProcessado) {
        if (valorProcessado.isEmpty()) {
            throw new DominioException("O e-mail não pode ser vazio.");
        }

        if (valorProcessado.length() < TAMANHO_MINIMO) {
            throw new DominioException(
                    "O e-mail deve ter pelo menos " + TAMANHO_MINIMO + " caracteres. Fornecido: '"
                            + valorProcessado + "' (" + valorProcessado.length() + ")"
            );
        }

        if (valorProcessado.length() > TAMANHO_MAXIMO) {
            throw new DominioException(
                    "O e-mail deve ter no máximo " + TAMANHO_MAXIMO + " caracteres. Fornecido: '"
                            + valorProcessado + "' (" + valorProcessado.length() + ")"
            );
        }

        if (!PADRAO_EMAIL.matcher(valorProcessado).matches()) {
            throw new DominioException(
                    "O formato do e-mail é inválido: '" + valorProcessado + "'"
            );
        }
    }
}

