CREATE TABLE IF NOT EXISTS t_mtu_moto (
  id_moto uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_modelo varchar(50) NOT NULL,
  nr_placa varchar(10) NOT NULL,
  nr_chassi varchar(25),
  fl_status char(1) NOT NULL,
  id_operador uuid,
  id_patio uuid,
  CONSTRAINT fk_moto_operador FOREIGN KEY (id_operador) REFERENCES t_mtu_operador (id_operador) ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT fk_moto_patio FOREIGN KEY (id_patio) REFERENCES t_mtu_patio (id_patio) ON UPDATE NO ACTION ON DELETE SET NULL
);

-- sensor
CREATE TABLE IF NOT EXISTS t_mtu_sensor (
  id_sensor uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_modelo varchar(50) NOT NULL,
  tp_sensor varchar(20) NOT NULL,
  nm_fabricante varchar(50),
  vs_firmware varchar(30),
  dt_instalacao timestamptz DEFAULT now(),
  dt_calibracao timestamptz,
  id_moto uuid,
  CONSTRAINT fk_sensor_moto FOREIGN KEY (id_moto) REFERENCES t_mtu_moto (id_moto) ON UPDATE NO ACTION ON DELETE CASCADE
);