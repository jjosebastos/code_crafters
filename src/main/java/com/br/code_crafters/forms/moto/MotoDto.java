package com.br.code_crafters.forms.moto;

import com.br.code_crafters.forms.sensor.SensorDto;
import com.br.code_crafters.forms.moto.Moto;
import com.br.code_crafters.forms.sensor.Sensor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotoDto {


    private UUID idMoto;

    @NotBlank(message = "{moto.validation.modelo.notblank}")
    private String nmModelo;

    @NotBlank(message = "{moto.validation.placa.notblank}")
    private String nrPlaca;

    private String nrChassi;

    @NotBlank(message = "{moto.validation.status.notblank}")
    private String flStatus;

    private UUID idOperador;
    private UUID idPatio;
    private SensorDto sensor;
    public MotoDto(Moto entity) {

        // Mapeamento dos campos diretos
        this.idMoto = entity.getIdMoto();
        this.nmModelo = entity.getNmModelo();
        this.nrPlaca = entity.getNrPlaca();
        this.nrChassi = entity.getNrChassi();
        this.flStatus = entity.getFlStatus();
        this.idOperador = entity.getOperador() != null ? entity.getOperador().getIdOperador() : null;
        this.idPatio = entity.getPatio() != null ? entity.getPatio().getIdPatio() : null;
    }
}
