package com.br.code_crafters.forms.monitoring;

import java.util.List;

public record PatioChartData(
        List<String> labels,
        List<Integer> data,
        Integer motosNoPatio,
        Integer motosForaDoPatio
) {}