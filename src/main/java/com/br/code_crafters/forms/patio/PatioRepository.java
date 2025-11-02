package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.monitoring.KpiChartDto;
import com.br.code_crafters.forms.moto.MotoSensorDto;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatioRepository extends JpaRepository<Patio, UUID>, JpaSpecificationExecutor<Patio> {
    Page<Patio> findByNmPatioContainingIgnoreCaseOrDsPatioContainingIgnoreCase(String nmPatio, String dsPatio, Pageable pageable);


    @Query(nativeQuery = true, value = """
        (SELECT
            'No Pátio' AS label,          -- Status 'D' (Disponível)
            COUNT(m.id_moto) AS value
         FROM t_mtu_moto m
         WHERE m.fl_status = 'D'
        )
        UNION ALL
        (SELECT
            'Fora do Pátio' AS label,  -- Status 'A' (Alugada/Ativa)
            COUNT(m.id_moto) AS value
         FROM t_mtu_moto m
         WHERE m.fl_status = 'A'
        )
        """)
    List<KpiChartDto> getPatiosChartData();

}