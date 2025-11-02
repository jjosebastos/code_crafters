ALTER TABLE t_mtu_endereco
DROP CONSTRAINT IF EXISTS fk_endereco_filial;

ALTER TABLE t_mtu_endereco
ADD CONSTRAINT fk_endereco_filial
    FOREIGN KEY (id_filial)
    REFERENCES t_mtu_filial (id_filial)
    ON DELETE CASCADE;