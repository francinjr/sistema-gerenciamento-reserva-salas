CREATE TABLE tb_salas
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(100)   NOT NULL,
    preco     NUMERIC(19, 2) NOT NULL,
    descricao VARCHAR(255),
    setor_id  BIGINT         NOT NULL,
    CONSTRAINT salas_nome_uk UNIQUE (nome),
    CONSTRAINT fk_sala_para_setor FOREIGN KEY (setor_id) REFERENCES tb_setores (id)
);