CREATE
EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE tb_agendamentos
(
    id                 BIGSERIAL PRIMARY KEY,
    data_hora_inicio   TIMESTAMP      NOT NULL,
    data_hora_fim      TIMESTAMP      NOT NULL,
    status             VARCHAR(50)    NOT NULL,
    quantidade_pessoas INT            NOT NULL,
    valor_total        NUMERIC(19, 2) NOT NULL,
    valor_sinal        NUMERIC(19, 2) NOT NULL,
    valor_finalizacao  NUMERIC(19, 2) NOT NULL,
    sala_id            BIGINT         NOT NULL,
    cliente_id         BIGINT         NOT NULL,

    CONSTRAINT fk_agendamento_sala FOREIGN KEY (sala_id) REFERENCES tb_salas (id),
    CONSTRAINT fk_agendamento_cliente FOREIGN KEY (cliente_id) REFERENCES tb_clientes (id),
    CONSTRAINT chk_datas_agendamento CHECK (data_hora_fim > data_hora_inicio),

    CONSTRAINT         agendamentos_conflito_de_horario_ex EXCLUDE USING gist (
        sala_id WITH =,
        tsrange(data_hora_inicio, data_hora_fim) WITH &&
    ) WHERE (status = 'CONFIRMADO')
);