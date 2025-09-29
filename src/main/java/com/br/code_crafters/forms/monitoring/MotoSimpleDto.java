package com.br.code_crafters.forms.monitoring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotoSimpleDto {
    private String placa;
    private String modelo;
    private String status;
}
