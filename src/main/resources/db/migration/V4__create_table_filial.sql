CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS t_mtu_filial (
  id_filial uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_filial varchar(50) NOT NULL,
  nr_cnpj varchar(18),
  ts_abertura timestamptz DEFAULT now(),
  cd_pais char(3)
);
