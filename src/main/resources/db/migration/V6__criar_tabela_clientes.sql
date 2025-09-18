/**
 * V5: Cria a tabela de clientes (tb_clientes).
 *
 * Esta tabela representa a entidade de negócio 'Cliente', associando
 * uma pessoa física a um usuário do sistema.
 */
CREATE TABLE tb_clientes
(
    id               BIGSERIAL PRIMARY KEY,
    pessoa_fisica_id BIGINT NOT NULL,
    usuario_id       BIGINT NOT NULL,

    -- Chaves estrangeiras para as tabelas correspondentes
    CONSTRAINT fk_cliente_pessoa_fisica FOREIGN KEY (pessoa_fisica_id) REFERENCES tb_pessoas_fisicas (id),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id),

    -- Constraints para garantir a relação um-para-um
    CONSTRAINT cliente_pessoa_fisica_id_uk UNIQUE (pessoa_fisica_id),
    CONSTRAINT cliente_usuario_id_uk UNIQUE (usuario_id)
);