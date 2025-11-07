CREATE TABLE t_mtu_vaga (
    id_vaga UUID NOT NULL,
    cd_codigo VARCHAR(10) NOT NULL,
    ds_status VARCHAR(20),
    id_patio UUID NOT NULL,
    CONSTRAINT pk_t_mtu_vaga PRIMARY KEY (id_vaga)
);

ALTER TABLE t_mtu_vaga
ADD CONSTRAINT uk_vaga_codigo UNIQUE (cd_codigo);

ALTER TABLE t_mtu_vaga
ADD CONSTRAINT fk_vaga_to_patio
FOREIGN KEY (id_patio)
REFERENCES t_mtu_patio (id_patio);

ALTER TABLE t_mtu_moto
ADD COLUMN nr_latitude DOUBLE PRECISION,
ADD COLUMN nr_longitude DOUBLE PRECISION;


ALTER TABLE t_mtu_moto
ADD COLUMN ds_status VARCHAR(255);