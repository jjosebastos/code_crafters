
CREATE TABLE t_mtu_user_profiles (
    user_id BIGINT PRIMARY KEY,
    sobrenome VARCHAR(100),
    telefone VARCHAR(20),
    bio VARCHAR(500),
    foto_url VARCHAR(255),
    idioma VARCHAR(10),
    tema VARCHAR(20),
    genero VARCHAR(30),
    notificacoes_ativas BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_user_profiles_to_users
        FOREIGN KEY (user_id)
        REFERENCES t_mtu_user(id)
        ON DELETE CASCADE
);