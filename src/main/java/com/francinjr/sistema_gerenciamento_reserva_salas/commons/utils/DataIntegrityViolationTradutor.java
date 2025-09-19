package com.francinjr.sistema_gerenciamento_reserva_salas.commons.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

@Component
public class DataIntegrityViolationTradutor {

    /**
     * Mapa que serve como um "dicionário" para traduzir nomes de constraints
     * do banco de dados para informações de erro de formulário.
     * Chave: Nome da constraint no SQL.
     * Valor: Um objeto ErrorInfo contendo o caminho do campo no DTO e a mensagem de erro.
     * Esse tipo de abordagem reduz a quantidade de código necessário para features e melhoram as entragas
     * das tarefas umas vez que as validações se tornam mais rápidas
     */
    private static final Map<String, ErrorInfo> CONSTRAINT_TO_ERROR_INFO_MAP = Map.ofEntries(
            // Constraints de Setor
            Map.entry("setores_nome_uk", new ErrorInfo("nome", "Este nome de setor já está em uso.")),

            // Constraints de Sala
            Map.entry("salas_nome_uk", new ErrorInfo("nome", "Este nome de sala já está em uso.")),

            // Constraints de PessoaFisica (caminhos aninhados)
            Map.entry("pessoas_fisicas_cpf_uk", new ErrorInfo("pessoaFisica.cpf", "O CPF informado já está em uso.")),
            Map.entry("pessoas_fisicas_telefone_uk", new ErrorInfo("pessoaFisica.telefone", "O telefone informado já está em uso.")),

            // Constraint de Usuario (caminho aninhado)
            Map.entry("usuarios_email_uk", new ErrorInfo("usuario.email", "O e-mail informado já está em uso.")),

            // Constraints de Recepcionista
            Map.entry("recepcionistas_setor_id_uk", new ErrorInfo("setorId", "O setor selecionado já possui um recepcionista associado.")),
            Map.entry("recepcionistas_pessoa_fisica_id_uk", new ErrorInfo("pessoaFisica", "Esta pessoa já está cadastrada como recepcionista.")),
            Map.entry("recepcionistas_usuario_id_uk", new ErrorInfo("usuario", "Este usuário já está associado a um recepcionista.")),

            // Constraint de Agendamento
            Map.entry("agendamentos_conflito_de_horario_ex", new ErrorInfo(
                    null, // Este é um erro global, não de um campo específico
                    "Conflito de horário! Já existe um agendamento confirmado para esta sala neste período."
            ))
    );

    /**
     * Metodo principal que recebe a exceção e o BindingResult do controller.
     * Ele inspeciona a mensagem de erro do banco, encontra a constraint correspondente
     * e adiciona um erro de formulário específico.
     */
    public void translate(DataIntegrityViolationException ex, BindingResult bindingResult) {
        String rootMessage = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse("");

        // Procura no dicionário qual constraint foi violada
        for (Map.Entry<String, ErrorInfo> entry : CONSTRAINT_TO_ERROR_INFO_MAP.entrySet()) {
            String constraintName = entry.getKey();
            ErrorInfo errorInfo = entry.getValue();

            if (rootMessage.toLowerCase().contains(constraintName.toLowerCase())) {
                // Se o fieldName for nulo, é um erro global. Senão, é um erro de campo.
                if (errorInfo.fieldName() != null) {
                    bindingResult.rejectValue(errorInfo.fieldName(), "erro.integridade", errorInfo.message());
                } else {
                    bindingResult.reject("erro.global", errorInfo.message());
                }
                return; // Encontrou o erro, pode parar
            }
        }

        // Se não encontrou uma constraint conhecida, adiciona um erro global genérico
        bindingResult.reject("erro.integridade", "Ocorreu um erro de integridade de dados não especificado.");
    }

    /**
     * Um 'record' privado para guardar os detalhes do erro de forma estruturada.
     */
    private record ErrorInfo(String fieldName, String message) {}
}