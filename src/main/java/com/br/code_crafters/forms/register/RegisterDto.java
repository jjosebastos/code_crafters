package com.br.code_crafters.forms.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String name;
    private String cpf;
    private String password;
    private String email;

    private Integer dia;
    private Integer mes;
    private Integer ano;

    private String genero;

    private String telefone;
}
