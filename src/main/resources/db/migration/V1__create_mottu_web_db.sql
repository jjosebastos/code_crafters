CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS t_mtu_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    username VARCHAR(50),
    password VARCHAR(72),
    role VARCHAR(10) DEFAULT 'USER',
    dateCreation TIMESTAMP DEFAULT NOW()
);

-- ==========================================================
-- Tabela de Filiais
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_filial (
  id_filial uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_filial varchar(50) NOT NULL,
  nr_cnpj varchar(18),
  ts_abertura timestamptz DEFAULT now(),
  cd_pais char(3),

  -- REQUERIMENTO: Check de país
  CONSTRAINT chk_filial_cd_pais CHECK (cd_pais IN ('BRA', 'MEX'))
);

-- ==========================================================
-- Tabela de Operadores (Consolidada)
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_operador (
  id_operador uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_operador varchar(50) NOT NULL,
  nr_cpf varchar(14) UNIQUE, -- Movido de ALTER para cá
  nr_rg varchar(20),
  dt_inscricao timestamptz DEFAULT now() -- Movido de ALTER para cá
);

-- ==========================================================
-- Tabela de Pátios
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_patio (
  id_patio uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_patio varchar(50) NOT NULL,
  ds_patio varchar(100),
  ts_created timestamptz DEFAULT now(),
  ts_update timestamptz,
  fl_aberto char(1),
  id_filial uuid,

  -- REQUERIMENTO: ON DELETE CASCADE
  CONSTRAINT fk_patio_filial
    FOREIGN KEY (id_filial)
    REFERENCES t_mtu_filial (id_filial)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

-- ==========================================================
-- Tabela de Motos (Consolidada)
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_moto (
  id_moto uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_modelo varchar(50) NOT NULL,
  nr_placa varchar(10) NOT NULL,
  nr_chassi varchar(25),
  fl_status char(1) NOT NULL DEFAULT 'D', -- Mantido o último DEFAULT ('D')
  id_operador uuid,
  id_patio uuid,

  -- REQUERIMENTO: Check de status
  CONSTRAINT chk_moto_fl_status CHECK (fl_status IN ('D', 'A', 'M', 'I')),

  -- REQUERIMENTO: ON DELETE CASCADE
  CONSTRAINT fk_moto_operador
    FOREIGN KEY (id_operador)
    REFERENCES t_mtu_operador (id_operador)
    ON UPDATE NO ACTION
    ON DELETE CASCADE,

  -- REQUERIMENTO: ON DELETE CASCADE
  CONSTRAINT fk_moto_patio
    FOREIGN KEY (id_patio)
    REFERENCES t_mtu_patio (id_patio)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

-- ==========================================================
-- Tabela de Sensores
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_sensor (
  id_sensor uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_modelo varchar(50) NOT NULL,
  tp_sensor varchar(20) NOT NULL,
  nm_fabricante varchar(50),
  vs_firmware varchar(30),
  dt_instalacao timestamptz DEFAULT now(),
  dt_calibracao timestamptz,
  id_moto uuid,

  -- REQUERIMENTO: ON DELETE CASCADE (Já estava correto)
  CONSTRAINT fk_sensor_moto
    FOREIGN KEY (id_moto)
    REFERENCES t_mtu_moto (id_moto)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

-- ==========================================================
-- Tabela de Endereços (Consolidada)
-- ==========================================================
CREATE TABLE IF NOT EXISTS t_mtu_endereco (
  id_endereco uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nm_logradouro varchar(50) NOT NULL,
  nr_logradouro varchar(5) NOT NULL,
  nm_bairro varchar(30) NOT NULL,
  nm_cidade varchar(20) NOT NULL,
  nm_uf char(2) NOT NULL,
  nr_cep varchar(9) NOT NULL, -- Ajustado para 9 (para incluir o '-')
  ds_complemento varchar(40),
  tp_endereco varchar(15),
  ts_created timestamptz DEFAULT now(),
  ts_update timestamptz,
  id_filial uuid NOT NULL,

  -- REQUERIMENTO: ON DELETE CASCADE
  CONSTRAINT fk_endereco_filial
    FOREIGN KEY (id_filial)
    REFERENCES t_mtu_filial (id_filial)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);