ALTER TABLE t_mtu_operador
ALTER COLUMN dt_inscricao SET DEFAULT now(),
ADD CONSTRAINT unique_cpf UNIQUE (nr_cpf);