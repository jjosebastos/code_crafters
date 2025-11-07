package com.br.code_crafters.forms.vaga;

import com.br.code_crafters.forms.patio.Patio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "t_mtu_vaga")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaga {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id_vaga", updatable = false, nullable = false)
    private UUID idVaga;

    @Column(name = "cd_codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name = "ds_status", length = 20)
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patio", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patio patio;


}