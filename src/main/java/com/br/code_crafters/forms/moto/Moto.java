package com.br.code_crafters.forms.moto;


import com.br.code_crafters.forms.sensor.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_mtu_moto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moto {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id_moto", updatable = false, nullable = false)
    private UUID idMoto;

    @Column(name = "nm_modelo", length = 50, nullable = false)
    private String nmModelo;

    @Column(name = "nr_placa", length = 10, nullable = false)
    private String nrPlaca;

    @Column(name = "nr_chassi", length = 25)
    private String nrChassi;

    @Column(name = "fl_status", length = 1, nullable = false, columnDefinition = "char(1) default 'S'")
    private String flStatus;

    @Column(name = "id_operador")
    private UUID idOperador;

    @Column(name = "id_patio")
    private UUID idPatio;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensores = new ArrayList<>();
}