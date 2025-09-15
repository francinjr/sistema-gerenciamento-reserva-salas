package com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

@Component
public class DataIntegrityViolationTranslator {

    /**
     * Mapa que serve como um "dicionário" para traduzir nomes de constraints
     * do banco de dados para nomes de campos de formulário.
     * * Chave: Nome da constraint no SQL.
     * Valor: Nome do campo no DTO/Entidade.
     */
    private static final Map<String, String> CONSTRAINT_TO_FIELD_MAP = Map.of(
            "setores_nome_uk", "nome"
            // Quando tiver um usuário, você adicionaria:
            // "usuarios_email_uk", "email",
            // "usuarios_cpf_uk", "cpf"
    );

    /**
     * Método principal que recebe a exceção e o BindingResult do controller.
     */
    public void translate(DataIntegrityViolationException ex, BindingResult bindingResult) {
        String rootMessage = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse("");

        // Procura no dicionário qual constraint foi violada
        for (Map.Entry<String, String> entry : CONSTRAINT_TO_FIELD_MAP.entrySet()) {
            String constraintName = entry.getKey();
            String fieldName = entry.getValue();

            if (rootMessage.toLowerCase().contains(constraintName.toLowerCase())) {
                String errorMessage = "O valor informado para '" + fieldName + "' já está em uso.";
                bindingResult.rejectValue(fieldName, "erro.duplicado", errorMessage);
                return; // Encontrou o erro, pode parar
            }
        }

        // Se não encontrou uma constraint conhecida, adiciona um erro global
        bindingResult.reject("erro.integridade", "Ocorreu um erro de integridade de dados não especificado.");
    }
}
