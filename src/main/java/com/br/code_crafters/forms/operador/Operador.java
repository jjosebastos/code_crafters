package com.br.code_crafters.forms.operador;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Table(name = "t_mtu_operador")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Operador {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id_operador")
    private UUID idOperador;

    @Column(name = "nm_operador")
    private String nome;

    @Column(name = "nr_cpf")
    private String cpf;

    @Column(name = "nr_rg")
    private String rg;
}
