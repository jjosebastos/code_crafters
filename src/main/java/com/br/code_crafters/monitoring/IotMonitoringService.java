package com.br.code_crafters.monitoring;


import com.br.code_crafters.forms.moto.Moto;
import com.br.code_crafters.forms.moto.MotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


@Service
@Slf4j
public class IotMonitoringService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MotoRepository motoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void processarAtualizacaoLocalizacao(String topic, String payload) {
        log.info("[IOT MONITORING SERVICE] Processando localização...");

        try {
            String motoIdStr = topic.split("/")[2];
            UUID motoId = UUID.fromString(motoIdStr);
            LocationUpdateDto locationData = objectMapper.readValue(payload, LocationUpdateDto.class);
            Moto moto = motoRepository.findById(motoId)
                    .orElseThrow(() -> new RuntimeException("Moto não encontrada: " + motoId));
            moto.setLatitude(locationData.getLat());
            moto.setLongitude(locationData.getLon());
            moto.setDsStatus(locationData.getStatus());
            Moto motoAtualizada = motoRepository.save(moto);
            String websocketPayload = objectMapper.writeValueAsString(motoAtualizada);
            messagingTemplate.convertAndSend("/topic/map-updates", websocketPayload);

        } catch (IllegalArgumentException e) {
            log.error("[IOT MONITORING SERVICE] Falha: O ID no tópico MQTT não é um UUID válido. Tópico: " + topic);
        } catch (Exception e) {
            log.error("[IOT MONITORING SERVICE] Falha ao processar localização: " + e.getMessage());
        }
    }

    private static class LocationUpdateDto {
        private double lat;
        private double lon;
        private String status;

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }
        public double getLon() { return lon; }
        public void setLon(double lon) { this.lon = lon; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}