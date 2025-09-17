CREATE TABLE tb_pessoas_fisicas
(
    id       BIGSERIAL PRIMARY KEY,
    nome     VARCHAR(255) NOT NULL,
    telefone VARCHAR(20)  NOT NULL,
    cpf      VARCHAR(11)  NOT NULL,
    CONSTRAINT pessoas_fisicas_cpf_uk UNIQUE (cpf),
    CONSTRAINT pessoas_fisicas_telefone_uk UNIQUE (telefone)
);