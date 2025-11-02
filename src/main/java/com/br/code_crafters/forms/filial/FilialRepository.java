package com.br.code_crafters.forms.filial;

import com.br.code_crafters.forms.monitoring.ChartData;
import com.br.code_crafters.forms.monitoring.KpiChartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilialRepository extends JpaRepository<Filial, UUID>, JpaSpecificationExecutor<Filial> {
    Page<Filial> findByNmFilialContainingIgnoreCaseOrNrCnpjContainingIgnoreCase(String nmFilial, String nrCnpj, Pageable pageable);
    @Query(nativeQuery = true,
            value = """
               SELECT 
                   e.nm_uf AS label,
                   COUNT(f.id_filial) AS value
               FROM t_mtu_filial f
               JOIN t_mtu_endereco e ON f.id_filial = e.id_filial
               GROUP BY e.nm_uf
               ORDER BY value DESC  -- Ordena pela contagem (maior primeiro)
               LIMIT 5             -- Limita aos 5 primeiros resultados
               """)
    List<KpiChartDto> getFilialCountByUf();
}
