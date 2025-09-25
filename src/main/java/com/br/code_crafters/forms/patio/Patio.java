package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.filial.Filial;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_mtu_patio")
@Data
public class Patio {

    @Id
    @GeneratedValue
    @Column(name = "id_patio", columnDefinition = "uuid")
    private UUID idPatio;

        @Column(name = "nm_patio", nullable = false, length = 50)
        private String nmPatio;

        @Column(name = "ds_patio", length = 100)
        private String dsPatio;

        @Column(name = "ts_created", columnDefinition = "timestamptz")
        private OffsetDateTime criadoEm = OffsetDateTime.now();

        @Column(name = "ts_update", columnDefinition = "timestamptz")
            private OffsetDateTime atualizadoEm;

        @Column(name = "fl_aberto", length = 1)
        private String flAberto;

        @ManyToOne
        @JoinColumn(name = "id_filial", foreignKey = @ForeignKey(name = "fk_patio_filial"))
        private Filial filiais;

}
