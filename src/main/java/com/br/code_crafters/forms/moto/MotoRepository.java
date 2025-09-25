package com.br.code_crafters.forms.moto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotoRepository extends JpaRepository<Moto, UUID> {
}
