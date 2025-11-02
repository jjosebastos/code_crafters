package com.br.code_crafters.forms.moto;

import com.br.code_crafters.forms.monitoring.KpiChartDto;
import com.br.code_crafters.forms.patio.Patio;
import jakarta.websocket.server.PathParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MotoRepository extends JpaRepository<Moto, UUID>, JpaSpecificationExecutor<Moto> {
    @Query("""
    SELECT new com.br.code_crafters.forms.moto.MotoSensorDto(
        m.idMoto, m.nmModelo, m.nrPlaca, m.nrChassi, m.flStatus,
        s.id, s.nmModelo, s.tpSensor, s.nmFabricante, s.vsFirmware, s.dtCalibracao
    )
    FROM Moto m
    LEFT JOIN Sensor s ON s.moto = m
    WHERE LOWER(m.nrPlaca) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.nmModelo) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    List<MotoSensorDto> findMotoSensorByPlacaOrModelo(@Param("search") String search);

    List<Moto> findByPatio_IdPatioOrderByNrPlacaAsc(UUID idPatio);

    List<Moto> findByPatioOrderByNrPlacaAsc(Patio patio);

    @Query(nativeQuery = true,
            value = """
        SELECT 
            t.nm_modelo AS label,
            COUNT(t.id_moto) AS value
        FROM t_mtu_moto t
        WHERE t.fl_status = 'A'
        GROUP BY t.nm_modelo
        """
    )
    List<KpiChartDto> findActivesPerModel();



}
