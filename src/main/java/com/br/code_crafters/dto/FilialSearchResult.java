package com.br.code_crafters.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilialSearchResult {

    private UUID idFilial;
    private String nome;
    private String cnpj;
    private LocalDateTime abertura;
    private String cdPais;
}
