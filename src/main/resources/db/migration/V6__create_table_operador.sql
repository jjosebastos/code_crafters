CREATE TABLE IF NOT EXISTS t_mtu_operador (
  id_operador uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_operador varchar(50) NOT NULL,
  nr_cpf varchar(14),
  nr_rg varchar(20),
  dt_inscricao timestamptz
);
