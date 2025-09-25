package com.br.code_crafters.forms.filial;


import com.br.code_crafters.config.MessageHelper;
import com.br.code_crafters.exception.CnpjAlreadyExistsException;
import com.br.code_crafters.forms.operador.Operador;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FilialService {

    private final FilialRepository filialRepository;


    public FilialService(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    public void save(FilialDto dto){
        try{
            var filial = mapperFilial(dto);
            filialRepository.save(filial);
        } catch (DataIntegrityViolationException ex){
            if(ex.getMostSpecificCause().getMessage().contains("cnpj_unique")){
                throw new CnpjAlreadyExistsException("CNPJ já cadastrado");
            }
            throw ex;
        }

    }

    public void update(UUID uuid, FilialDto dto){
        try {
            var found = filialRepository.findById(uuid).orElseThrow();
            found.setNmFilial(dto.getNmFilial());
            found.setNrCnpj(dto.getNrCnpj());
            found.setCdPais(dto.getCdPais());
            filialRepository.save(found);
        } catch (DataIntegrityViolationException e){
            if(e.getMostSpecificCause().getMessage().contains("cnpj_unique")){
                throw new CnpjAlreadyExistsException("CNPJ já cadastrado");
            }
            throw e;
        }
    }

    public Page<Filial> findByNomeOrCnpj(String nome, String cnpj, Pageable pageable){
        return filialRepository.findByNmFilialContainingIgnoreCaseOrNrCnpjContainingIgnoreCase(nome, cnpj, pageable);
    }

    public Page<Filial> findAll(Pageable pageable){
        return filialRepository.findAll(pageable);
    }


    public void deleteById(UUID id){
        filialRepository.deleteById(id);
    }

    public Optional<Filial> findById(UUID id){
        return this.filialRepository.findById(id);
    }

    private Filial mapperFilial(FilialDto dto){
        return Filial.builder()
                .nmFilial(dto.getNmFilial())
                .nrCnpj(dto.getNrCnpj())
                .cdPais(dto.getCdPais())
                .build();
    }
}
