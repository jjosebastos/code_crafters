package com.br.code_crafters.forms.patio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class PatioDto {

    private UUID idPatio;

    @NotBlank(message = "{nome.patio.notblank}")
    private String nmPatio;

    @NotBlank(message = "{descricao.patio.notblank}")
    private String dsPatio;

    @NotNull(message = "{filial.patio.notnull}")
    private UUID idFilial;
}