package com.br.code_crafters.monitoringiot;

import java.util.UUID;

public record VagaUpdateDto(
        UUID idVaga,
        String codigo,
        String status
) {
}