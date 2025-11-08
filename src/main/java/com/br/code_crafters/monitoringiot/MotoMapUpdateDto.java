package com.br.code_crafters.monitoringiot;

import java.util.UUID;

public record MotoMapUpdateDto(
        UUID idMoto,
        Double latitude,
        Double longitude,
        String nrPlaca,
        String nmModelo,
        String dsStatus
) {
}