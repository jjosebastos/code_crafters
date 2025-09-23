CREATE TABLE IF NOT EXISTS t_mtu_patio (
  id_patio uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_patio varchar(50) NOT NULL,
  ds_patio varchar(100),
  ts_created timestamptz DEFAULT now(),
  ts_update timestamptz,
  fl_aberto char(1),
  id_filial uuid,
  CONSTRAINT fk_patio_filial FOREIGN KEY (id_filial) REFERENCES t_mtu_filial (id_filial) ON UPDATE NO ACTION ON DELETE SET NULL
);