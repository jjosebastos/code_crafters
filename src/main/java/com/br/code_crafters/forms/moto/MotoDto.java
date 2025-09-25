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

    // Removido @NotBlank, pois chassi pode ser null no DB.
    // Se quiser validar o tamanho, use @Size(max=25, message="{moto.validation.chassi.size}")
    private String chassi;

    @NotBlank(message = "{moto.validation.status.notblank}")
    private String status;

    private UUID idOperador; // Pode ser null
    private UUID idPatio;    // Pode ser null
    private SensorDto sensor;
}
