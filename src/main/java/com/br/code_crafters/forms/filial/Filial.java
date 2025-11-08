package com.br.code_crafters.forms.filial;

import com.br.code_crafters.forms.endereco.Endereco;
import com.br.code_crafters.forms.patio.Patio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "t_mtu_filial")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_filial", updatable = false, nullable = false)
    private UUID idFilial;

    @Column(name = "nm_filial")
    @NotBlank(message = "O nome da filial não pode ser vazio.")
    private String nmFilial;

    @Column(name = "nr_cnpj")
    @NotBlank(message = "O CNPJ não pode ser vazio.")
    private String nrCnpj;

    @Column(name = "cd_pais")
    @NotBlank(message = "O código do país não pode ser vazio.")
    private String cdPais;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patio> patios;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;
}