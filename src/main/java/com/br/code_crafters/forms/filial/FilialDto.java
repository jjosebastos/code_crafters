package com.br.code_crafters.forms.filial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class FilialDto {

    private UUID idFilial;

    @NotBlank(message = "{filial.name.notblank}")
    private String nmFilial;

    @Size(min = 18, max = 18, message = "{filial.cnpj.invalid.size}")
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", message = "{filial.cnpj.invalid.format}")
    private String nrCnpj;

    @NotBlank(message = "{filial.cdpais.invalid}")
    @Size(max = 3, message = "{filial.cdpais.size}")
    private String cdPais;

    @NotBlank(message = "{endereco.rua.notblank}")
    private String nmLogradouro;

    @NotBlank(message = "{endereco.numero.notblank}")
    private String nrLogradouro;

    @NotBlank(message = "{endereco.bairro.notblank}")
    private String nmBairro;

    @NotBlank(message = "{endereco.cidade.notblank}")
    private String nmCidade;

    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "{filial.cep.invalid.format}")
    @NotBlank(message = "{endereco.cep.notblank}")
    private String nrCep;

    @Size(max = 2, min = 2)
    @NotBlank(message = "{endereco.uf.notblank}")
    private String nmUf;

    private String dsComplemento;
}