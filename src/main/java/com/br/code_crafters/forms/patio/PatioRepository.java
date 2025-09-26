package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.moto.MotoSensorDto;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatioRepository extends JpaRepository<Patio, UUID>, JpaSpecificationExecutor<Patio> {




    Page<Patio> findByNmPatioContainingIgnoreCaseOrDsPatioContainingIgnoreCase(String nmPatio, String dsPatio, Pageable pageable);
}