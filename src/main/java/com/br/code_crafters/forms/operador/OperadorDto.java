package com.br.code_crafters.forms.operador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperadorDto {

    private UUID idOperador;

    @NotBlank(message = "{operador.name.notblank}")
    private String nome;

    @NotBlank(message = "{operador.cpf.notblank}")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido. O formato deve ser XXX.XXX.XXX-XX")
    private String cpf;

    @NotBlank(message = "{operador.rg.notblank}")
    private String rg;
}