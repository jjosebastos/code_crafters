package com.br.code_crafters.forms.ajustes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AjustesDto {

    private String fotoPerfilUrl;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String bio;
    private String idioma;
    private String tema;
    private String genero;
    private boolean notificacoesAtivas;

    public AjustesDto(){}
}

