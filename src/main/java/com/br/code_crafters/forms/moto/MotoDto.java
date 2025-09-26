package com.br.code_crafters.forms.moto;

import com.br.code_crafters.forms.sensor.SensorDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotoDto {


    private UUID id;

    @NotBlank(message = "{moto.validation.modelo.notblank}")
    private String modelo;

    @NotBlank(message = "{moto.validation.placa.notblank}")
    private String placa;

    private String chassi;

    @NotBlank(message = "{moto.validation.status.notblank}")
    private String status;

    private UUID idOperador;
    private UUID idPatio;
    private SensorDto sensor;
}
