package com.br.code_crafters.filial;


import com.br.code_crafters.config.MessageHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
public class FilialService {

    private final FilialRepository filialRepository;
    private final MessageHelper messageHelper;

    public FilialService(FilialRepository filialRepository, MessageHelper messageHelper) {
        this.filialRepository = filialRepository;
        this.messageHelper = messageHelper;
    }

    public List<Filial> searchByNameOrCnpj(String q, Pageable page) {
        if (page == null) {
            page = PageRequest.of(0, 10, Sort.by("nome"));
        }
        var found = this.filialRepository.findByNameOrCnpj(q, page);
        return found.getContent(); // Use getContent() to get the list
    }


    public List<Filial> getFiliaisOrdenadas() {
        List<Filial> filiais = filialRepository.findAll();
        filiais.sort(Comparator.comparing(Filial::getAbertura, Comparator.nullsLast(Comparator.naturalOrder())));
        return filiais;
    }
    @Transactional
    public void create(Filial filial){
        filialRepository.save(filial);
    }

    public Page<Filial> findPaginated(Pageable pageable){
        return this.filialRepository.findAll(pageable);
    }

    public Filial update(UUID id, Filial filialAtualizada) {
        Filial filialExistente = filialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Filial não encontrada com o ID: " + id));

        filialExistente.setNome(filialAtualizada.getNome());
        filialExistente.setCnpj(filialAtualizada.getCnpj());
        filialExistente.setCdPais(filialAtualizada.getCdPais());

        return filialRepository.save(filialExistente);
    }

    public void delete(UUID id, Filial filial){
        filialRepository.deleteById(id);
    }

    public Filial findById(UUID id){
        return this.filialRepository.findById(id).orElseThrow();
    }
}
