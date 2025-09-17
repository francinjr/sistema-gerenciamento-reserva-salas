CREATE TABLE tb_usuarios
(
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    papel VARCHAR(50)  NOT NULL,
    CONSTRAINT usuarios_email_uk UNIQUE (email)
);