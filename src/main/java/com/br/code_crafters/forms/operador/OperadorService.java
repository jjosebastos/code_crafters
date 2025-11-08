package com.br.code_crafters.forms.operador;

import com.br.code_crafters.exception.CpfAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OperadorService {

    private final OperadorRepository operadorRepository;

    public OperadorService(OperadorRepository operadorRepository) {
        this.operadorRepository = operadorRepository;
    }

    public void save(OperadorDto operadorDto){

        try{
            var operador = mapperOperador(operadorDto);
            operadorRepository.save(operador);

        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getMostSpecificCause();
            if (rootCause instanceof SQLException sqlEx && "23505".equals(sqlEx.getSQLState())) {
                if (sqlEx.getMessage().toLowerCase().contains("nr_cpf")) {
                    throw new CpfAlreadyExistsException("CPF já cadastrado.");
                }
            }
            throw e;
        }

    }

    public void update(UUID id, OperadorDto dto){

        try{
            var found = this.operadorRepository.findById(id).orElseThrow();
            found.setCpf(dto.getCpf());
            found.setNome(dto.getNome());
            found.setRg(dto.getRg());
            operadorRepository.save(found);

        } catch (DataIntegrityViolationException ex){
            Throwable rootCause = ex.getMostSpecificCause();
            if (rootCause instanceof SQLException sqlEx && "23505".equals(sqlEx.getSQLState())) {
                if (sqlEx.getMessage().toLowerCase().contains("nr_cpf")) {
                    throw new CpfAlreadyExistsException("CPF já cadastrado.");
                }
            }
            throw ex;
        }

    }

    public void deleteById(UUID id){
        operadorRepository.deleteById(id);
    }

    public Page<Operador> findAll(Pageable pageable){
        return operadorRepository.findAll(pageable);
    }

    public Page<Operador> findByNomeOrCpf(String nome, String cpf,Pageable pageable) {
        return operadorRepository.findByNomeContainingIgnoreCaseOrCpfContainingIgnoreCase(nome, cpf , pageable);
    }


    public Optional<Operador> findById(UUID id){
        return operadorRepository.findById(id);
    }


    private Operador mapperOperador(OperadorDto dto){
        return Operador.builder()
                .cpf(dto.getCpf())
                .nome(dto.getNome())
                .rg(dto.getRg())
                .build();
    }
}