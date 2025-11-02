CREATE TABLE IF NOT EXISTS t_mtu_endereco (
  id_endereco uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_logradouro varchar(50) NOT NULL,
  nr_logradouro varchar(5) NOT NULL,
  nm_bairro varchar(30) NOT NULL,
  nm_cidade varchar(20) NOT NULL,
  nm_uf char(2) NOT NULL,
  nr_cep varchar(8) NOT NULL,
  ds_complemento varchar(40),
  tp_endereco varchar(15),
  ts_created timestamptz DEFAULT now(),
  ts_update timestamptz,
  id_filial uuid NOT NULL,
  CONSTRAINT fk_endereco_filial
    FOREIGN KEY (id_filial)
    REFERENCES t_mtu_filial (id_filial)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);