ALTER TABLE t_mtu_moto
ALTER COLUMN fl_status SET DEFAULT 'S';

UPDATE t_mtu_moto
SET fl_status = 'S'
WHERE fl_status IS NULL;