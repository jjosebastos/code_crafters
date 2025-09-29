package com.br.code_crafters.forms.sensor;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

@Data
public class SensorDto {
    @NotBlank(message = "{sensor.validation.modelo.notblank}")
    private String modelo;
    @NotBlank(message = "{sensor.validation.tipo.notblank}")
    private String tipo;
    @NotBlank(message = "{sensor.validation.fabricante.notblank}")
    private String fabricante;
    @NotBlank(message = "{sensor.validation.firmware.notblank}")
    private String firmware;
    private OffsetDateTime dataCalibracao; // Agora é LocalDate


    public SensorDto(Sensor entity) {
        this.modelo = entity.getNmModelo();
        this.tipo = entity.getTpSensor();
        this.firmware = entity.getVsFirmware();

        // Mapeamento de data:
        // Se a entidade Sensor usa OffsetDateTime e você quer apenas a data:
        if (entity.getDtCalibracao() != null) {
            this.dataCalibracao = entity.getDtCalibracao();
        } else {
            this.dataCalibracao = null;
        }

    }
}