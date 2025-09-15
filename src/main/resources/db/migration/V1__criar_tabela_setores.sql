CREATE TABLE tb_setores
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    CONSTRAINT setores_nome_uk UNIQUE (nome)
);