package com.francinjr.sistema_gerenciamento_reserva_salas.commons.domain.valueobjects;

import com.francinjr.sistema_gerenciamento_reserva_salas.commons.exceptions.DominioException;
import jakarta.persistence.Embeddable;
import java.util.InputMismatchException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class Cpf {

    private String valor;

    public Cpf(String valor) {
        String valorNormalizado = normalizar(valor);
        validar(valorNormalizado);

        this.valor = valorNormalizado;
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isEmpty()) {
            throw new DominioException("CPF precisa ser informado.");
        }
        return valor.replaceAll("[^\\d]", "");
    }

    private void validar(String valor) {
        if (!cpfValido(valor)) {
            throw new DominioException("CPF inválido.");
        }
    }

    private boolean cpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }

        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") ||
                cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") ||
                cpf.equals("99999999999")) {
            return false;
        }

        try {
            char digito10, digito11;
            int soma, i, resto, numero, peso;

            soma = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                numero = cpf.charAt(i) - '0';
                soma += numero * peso;
                peso--;
            }

            resto = 11 - (soma % 11);
            digito10 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            soma = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                numero = cpf.charAt(i) - '0';
                soma += numero * peso;
                peso--;
            }

            resto = 11 - (soma % 11);
            digito11 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            return digito10 == cpf.charAt(9) && digito11 == cpf.charAt(10);
        } catch (InputMismatchException e) {
            return false;
        }
    }

    public String getValorFormatado() {
        return String.format("%s.%s.%s-%s",
                valor.substring(0, 3),
                valor.substring(3, 6),
                valor.substring(6, 9),
                valor.substring(9, 11));
    }

    @Override
    public String toString() {
        return getValorFormatado();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf)) return false;
        Cpf outro = (Cpf) o;
        return valor.equals(outro.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }
}