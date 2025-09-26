package com.br.code_crafters.forms.patio;

import com.br.code_crafters.forms.filial.Filial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import java.time.OffsetDateTime;

@Entity
@Table(name = "t_mtu_patio")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patio {

    @Id
    @GeneratedValue
    @UuidGenerator @Column(name = "id_patio")
    private UUID idPatio;

    @Column(name = "nm_patio", nullable = false, length = 50)
    private String nmPatio;

    @Column(name = "ds_patio", length = 100)
    private String dsPatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_filial", foreignKey = @ForeignKey(name = "fk_patio_filial"))
    private Filial filial;
}
