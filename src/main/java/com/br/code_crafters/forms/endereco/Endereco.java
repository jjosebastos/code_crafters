package com.br.code_crafters.forms.endereco;

import com.br.code_crafters.forms.filial.Filial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Table(name = "t_mtu_endereco")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(name = "id_endereco")
    private UUID idEndereco;

    @Column(name = "nm_logradouro")
    private String nmLogradouro;

    @Column(name = "nr_logradouro")
    private String nrLogradouro;

    @Column(name = "nm_bairro")
    private String nmBairro;

    @Column(name = "nm_cidade")
    private String nmCidade;

    @Column(name = "nr_cep")
    private String nrCep;

    @Column(name = "nm_uf")
    private String nmUf;

    @Column(name = "ds_complemento")
    private String dsComplemento;

    @JoinColumn(name = "id_filial", foreignKey = @ForeignKey(name = "fk_filial_endereco"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Filial filial;



}
