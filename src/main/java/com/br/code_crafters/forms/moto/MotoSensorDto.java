package com.br.code_crafters.forms.moto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MotoSensorDto {
    private UUID idMoto;
    private String nmModelo;
    private String nrPlaca;
    private String nrChassi;
    private UUID idSensor;
    private String nmModeloSensor;
    private String tpSensor;
    private String nmFabricante;
    private String vsFirmware;
    private OffsetDateTime dtCalibracao;
}
