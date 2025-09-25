package com.br.code_crafters.forms.filial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilialRepository extends JpaRepository<Filial, UUID>, JpaSpecificationExecutor<Filial> {


    Page<Filial> findByNmFilialContainingIgnoreCaseOrNrCnpjContainingIgnoreCase(String nmFilial, String nrCnpj, Pageable pageable);
}
