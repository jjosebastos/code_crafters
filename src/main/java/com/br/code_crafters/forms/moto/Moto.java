package com.br.code_crafters.forms.moto;


import com.br.code_crafters.forms.operador.Operador;
import com.br.code_crafters.forms.patio.Patio;
import com.br.code_crafters.forms.sensor.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operador")
    public Operador operador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patio")
    public Patio patio;

    @Column(name = "fl_status")
    private String flStatus;

    @Column(name = "nr_latitude")
    private Double latitude;

    @Column(name = "nr_longitude")
    private Double longitude;

    @Column(name = "ds_status")
    private String dsStatus;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensores;
}