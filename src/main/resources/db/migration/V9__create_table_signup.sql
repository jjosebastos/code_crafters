CREATE TABLE t_mtu_signup (
    id_signup BIGINT PRIMARY KEY,
    vl_password VARCHAR(72) NOT NULL,
    tp_role     VARCHAR(5) NOT NULL,
    nm_login    VARCHAR(50) UNIQUE NOT NULL,
    nm_full     VARCHAR(100) NOT NULL,
    CHECK (tp_role in ('ADMIN','USER'))
);