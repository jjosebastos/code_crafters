package com.br.code_crafters.forms.filial;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Table(name = "t_mtu_filial")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Filial {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(name = "id_filial")
    private UUID id;


    @Column(name = "nm_filial")
    @NotBlank(message = "O nome da filial não pode ser vazio.")
    private String nome;

    @Column(name = "nr_cnpj")
    @NotBlank(message = "O nome da filial não pode ser vazio.")
    private String cnpj;

    @Column(name = "cd_pais")
    @NotBlank(message = "O nome da filial não pode ser vazio.")
    private String cdPais;

    @Column(name = "ts_abertura", updatable = false, insertable = false)
    private LocalDateTime abertura;


}
