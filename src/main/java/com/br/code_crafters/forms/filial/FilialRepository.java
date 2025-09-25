package com.br.code_crafters.forms.filial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilialRepository extends JpaRepository<Filial, UUID> {

    @Query("SELECT f FROM Filial f " +
            "WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "   OR LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "ORDER BY f.nome")
    Page<Filial> findByNameOrCnpj(@Param("q") String q, Pageable pageable);
}
