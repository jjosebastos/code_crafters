package com.br.code_crafters.forms.moto;

import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MotoRepository extends JpaRepository<Moto, UUID>, JpaSpecificationExecutor<Moto> {
    @Query("""
        SELECT new com.br.code_crafters.forms.moto.MotoSensorDto(
            m.idMoto, m.nmModelo, m.nrPlaca, m.nrChassi,
            s.id, s.nmModelo, s.tpSensor, s.nmFabricante, s.vsFirmware, s.dtCalibracao
        )
        FROM Moto m
        LEFT JOIN Sensor s ON s.moto = m
        WHERE m.nrPlaca = :placa
        """)
    MotoSensorDto findMotoSensorByPlaca(@Param("placa") String placa);

}
