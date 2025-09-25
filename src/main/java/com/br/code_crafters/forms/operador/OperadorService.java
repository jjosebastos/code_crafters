package com.br.code_crafters.forms.operador;

import com.br.code_crafters.exception.CpfAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            // Verifique a mensagem da exceção para confirmar que é uma violação de chave única
            if (e.getMostSpecificCause().getMessage().contains("unique_cpf")) {
                throw new CpfAlreadyExistsException("CPF já cadastrado.");
            }
            throw e; // Re-lança a exceção original se não for o caso do CPF duplicado
        }

    }

    public void update(UUID id, OperadorDto dto){

        try{
            var found = this.operadorRepository.findById(id).orElseThrow();
            found.setCpf(dto.getCpf());
            found.setNome(dto.getNome());
            found.setNome(dto.getNome());
            operadorRepository.save(found);
        } catch (DataIntegrityViolationException ex){
            if(ex.getMostSpecificCause().getMessage().contains("unique_cof")){
                throw new CpfAlreadyExistsException("CPF já cadastrado.");
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
