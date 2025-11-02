package com.br.code_crafters.forms.monitoring;

import java.util.List;

public record ChartData(List<String> labels, List<Integer> data) {
}
