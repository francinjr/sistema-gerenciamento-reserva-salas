CREATE TABLE tb_recepcionistas
(
    id               BIGSERIAL PRIMARY KEY,
    setor_id         BIGINT NOT NULL,
    pessoa_fisica_id BIGINT NOT NULL,
    usuario_id       BIGINT NOT NULL,
    CONSTRAINT fk_recepcionista_setor FOREIGN KEY (setor_id) REFERENCES tb_setores (id),
    CONSTRAINT fk_recepcionista_pessoa_fisica FOREIGN KEY (pessoa_fisica_id) REFERENCES tb_pessoas_fisicas (id),
    CONSTRAINT fk_recepcionista_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios (id),
    CONSTRAINT recepcionistas_setor_id_uk UNIQUE (setor_id),
    CONSTRAINT recepcionistas_pessoa_fisica_id_uk UNIQUE (pessoa_fisica_id),
    CONSTRAINT recepcionistas_usuario_id_uk UNIQUE (usuario_id)
);