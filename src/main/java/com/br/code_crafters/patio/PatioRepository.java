package com.br.code_crafters.patio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatioRepository extends JpaRepository<Patio, UUID> {
}
