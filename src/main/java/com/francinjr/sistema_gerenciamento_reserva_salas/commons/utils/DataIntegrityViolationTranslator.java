package com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

@Component
public class DataIntegrityViolationTranslator {

    private static final Map<String, ErrorInfo> CONSTRAINT_TO_ERROR_INFO_MAP = Map.of(
            // Constraints de Setor/Sala (o caminho é direto)
            "setores_nome_uk", new ErrorInfo("nome", "Este nome de setor já está em uso."),
            "salas_nome_uk", new ErrorInfo("nome", "Este nome de sala já está em uso."),

            // Constraints de PessoaFisica (o caminho é aninhado)
            "pessoas_fisicas_cpf_uk", new ErrorInfo("pessoaFisica.cpf", "O CPF informado já está em uso."),
            "pessoas_fisicas_telefone_uk", new ErrorInfo("pessoaFisica.telefone", "O telefone informado já está em uso."),

            // Constraint de Usuario (o caminho é aninhado)
            "usuarios_email_uk", new ErrorInfo("usuario.email", "O e-mail informado já está em uso."),

            // Constraint de Recepcionista (o caminho é direto)
            "recepcionistas_setor_id_uk", new ErrorInfo("setorId", "O setor selecionado já possui um recepcionista associado.")
    );

    public void translate(DataIntegrityViolationException ex, BindingResult bindingResult) {
        String rootMessage = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse("");

        for (Map.Entry<String, ErrorInfo> entry : CONSTRAINT_TO_ERROR_INFO_MAP.entrySet()) {
            String constraintName = entry.getKey();
            ErrorInfo errorInfo = entry.getValue();

            if (rootMessage.toLowerCase().contains(constraintName.toLowerCase())) {
                // ✅ CORRIGIDO: Usa o caminho completo do campo (ex: "pessoaFisica.cpf")
                bindingResult.rejectValue(errorInfo.fieldName(), "erro.duplicado", errorInfo.message());
                return;
            }
        }

        bindingResult.reject("erro.integridade", "Ocorreu um erro de integridade de dados não especificado.");
    }

    /**
     * ✅ NOVO: Um 'record' privado para guardar os detalhes do erro de forma estruturada.
     */
    private record ErrorInfo(String fieldName, String message) {}
}