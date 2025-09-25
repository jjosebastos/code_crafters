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
    private String cdPais;
}
