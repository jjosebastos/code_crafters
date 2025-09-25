package com.br.code_crafters.forms.operador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperadorRepository extends JpaRepository<Operador, UUID>, JpaSpecificationExecutor<Operador> {

    Page<Operador> findByNomeContainingIgnoreCaseOrCpfContainingIgnoreCase(String nome, String cpf, Pageable pageable);

    Page<Operador> findAll(Pageable pageable);
}
