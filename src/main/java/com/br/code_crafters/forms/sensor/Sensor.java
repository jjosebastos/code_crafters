package com.br.code_crafters.forms.sensor;


import com.br.code_crafters.forms.moto.Moto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_mtu_sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id_sensor", updatable = false, nullable = false)
    private UUID idSensor;

    @Column(name = "nm_modelo", length = 50, nullable = false)
    private String nmModelo;

    @Column(name = "tp_sensor", length = 20, nullable = false)
    private String tpSensor;

    @Column(name = "nm_fabricante", length = 50)
    private String nmFabricante;

    @Column(name = "vs_firmware", length = 30)
    private String vsFirmware;

    @Column(name = "dt_calibracao")
    private OffsetDateTime dtCalibracao;

    @OneToOne
    @JoinColumn(name = "id_moto") // ou o nome real da FK em sensor
    private Moto moto;
}
