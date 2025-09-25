package com.br.code_crafters.forms.patio;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PatioService {

    private final PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }


    @Transactional
    public Patio create(Patio patio){
        return patioRepository.save(patio);
    }

    public List<Patio> findAll(){
        return this.patioRepository.findAll();
    }

}
