-- Adiciona o status à tabela de setores, com FECHADO como padrão
ALTER TABLE tb_setores
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'FECHADO';

-- Adiciona a data de finalização à tabela de agendamentos (pode ser nula)
ALTER TABLE tb_agendamentos
    ADD COLUMN data_hora_finalizacao TIMESTAMP;