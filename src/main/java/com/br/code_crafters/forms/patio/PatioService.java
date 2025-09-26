package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.filial.Filial;
import com.br.code_crafters.forms.filial.FilialRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatioService {

    private final PatioRepository patioRepository;
    private final FilialRepository filialRepository;

    public PatioService(PatioRepository patioRepository, FilialRepository filialRepository) {
        this.patioRepository = patioRepository;
        this.filialRepository = filialRepository;
    }

    @Transactional
    public PatioDto savePatio(PatioDto dto) {
        Patio patio = mapDtoToEntity(dto);
        Patio savedPatio = patioRepository.save(patio);
        return mapEntityToDto(savedPatio);
    }

    public Page<Patio> findAll(Pageable pageable) {
        return patioRepository.findAll(pageable);
    }

    public List<Filial> finAllFiliais(){
        return filialRepository.findAll();
    }

    public Optional<Patio> findById(UUID id) {
        return patioRepository.findById(id);
    }

    @Transactional
    public PatioDto updatePatio(UUID idPatio, PatioDto dto) {
        Optional<Patio> existingPatioOpt = patioRepository.findById(idPatio);
        if (existingPatioOpt.isEmpty()) {
            throw new RuntimeException("Patio not found with ID: " + idPatio);
        }

        Patio existingPatio = existingPatioOpt.get();
        existingPatio.setNmPatio(dto.getNmPatio());
        existingPatio.setDsPatio(dto.getDsPatio());
        Filial filial = null;
        if (dto.getIdFilial() != null) {
            var optionalFilial = filialRepository.findById(dto.getIdFilial());
            filial = optionalFilial.orElseThrow(() -> new RuntimeException("Filial not found with ID: " + dto.getIdFilial()));
        }
        existingPatio.setFilial(filial);

        Patio updatedPatio = patioRepository.save(existingPatio);
        return mapEntityToDto(updatedPatio);
    }

    public Page<Patio> findAllByNomeDescricao(String nome, String descricao, Pageable pageable){
        return patioRepository.
                findByNmPatioContainingIgnoreCaseOrDsPatioContainingIgnoreCase(nome, descricao, pageable);
    }

    @Transactional
    public void deletePatio(UUID id) {
        patioRepository.deleteById(id);
    }



    private Patio mapDtoToEntity(PatioDto dto) {
          Filial filial = null;
        if (dto.getIdFilial() != null) {
            Optional<Filial> optionalFilial = filialRepository.findById(dto.getIdFilial());
            filial = optionalFilial.orElseThrow(() -> new RuntimeException("Filial not found with ID: " + dto.getIdFilial()));
        }

        return Patio.builder()
                .idPatio(dto.getIdPatio()) // Se for um update, mantém o ID existente
                .nmPatio(dto.getNmPatio())
                .dsPatio(dto.getDsPatio())
                .filial(filial) // Seta a entidade Filial COMPLETA
                .build();
    }


        private PatioDto mapEntityToDto(Patio patio) {
            PatioDto dto = new PatioDto();
            dto.setIdPatio(patio.getIdPatio());
            dto.setNmPatio(patio.getNmPatio());
            dto.setDsPatio(patio.getDsPatio());
            if (patio.getFilial() != null) {
                dto.setIdFilial(patio.getFilial().getIdFilial());
            }
            return dto;
        }
}
