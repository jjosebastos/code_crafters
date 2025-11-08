package com.br.code_crafters.monitoringiot;

import com.br.code_crafters.forms.vaga.Vaga;
import com.br.code_crafters.forms.vaga.VagaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class IotVagaService {

    private final SimpMessagingTemplate messagingTemplate;
    private final VagaRepository vagaRepository;

    public IotVagaService(SimpMessagingTemplate messagingTemplate, VagaRepository vagaRepository) {
        this.messagingTemplate = messagingTemplate;
        this.vagaRepository = vagaRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void processarAtualizacaoVagaFromMqtt(String topic, String payload) {
        log.info("Processando status de vaga para o tópico: {}", topic);

        try {
            String vagaCodigo = topic.split("/")[4];

            VagaStatusDto statusDto = objectMapper.readValue(payload, VagaStatusDto.class);
            String novoStatus = statusDto.getStatus();

            Vaga vaga = vagaRepository.findByCodigo(vagaCodigo)
                    .orElseThrow(() -> new RuntimeException("Vaga não encontrada: " + vagaCodigo));

            vaga.setStatus(novoStatus);

            Vaga vagaAtualizada = vagaRepository.save(vaga);

            VagaUpdateDto vagaDto = new VagaUpdateDto(
                    vagaAtualizada.getIdVaga(),
                    vagaAtualizada.getCodigo(),
                    vagaAtualizada.getStatus()
            );

            String websocketPayload = objectMapper.writeValueAsString(vagaDto);
            messagingTemplate.convertAndSend("/topic/vaga-updates", websocketPayload);

        } catch (Exception e) {
            log.error("Falha ao processar vaga: {}", e.getMessage());
        }
    }

    private static class VagaStatusDto {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}