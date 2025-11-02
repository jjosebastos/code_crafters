package com.br.code_crafters.user;

import com.br.code_crafters.forms.monitoring.KpiChartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = """
    SELECT 
        TO_CHAR(t.dateCreation, 'YYYY-MM') AS label, 
        COUNT(t.id) AS value
    FROM t_mtu_user t
    WHERE t.dateCreation >= (CURRENT_DATE - INTERVAL '6 months')
    GROUP BY 1  
    ORDER BY 1 
    """, nativeQuery = true)
    List<KpiChartDto> findRegistrationsPerMonth();
}
