package com.br.code_crafters.forms.monitoring;

import com.br.code_crafters.forms.filial.FilialRepository;
import com.br.code_crafters.forms.moto.MotoRepository;
import com.br.code_crafters.user.UserRepository;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardService {

    public interface KpiData {
        String getLabel();
        Long getValue();
    }

    private final UserRepository userRepository;
    private final FilialRepository filialRepository;
    private final MotoRepository motoRepository;

    public DashboardService(UserRepository userRepository, FilialRepository filialRepository, MotoRepository motoRepository) {
        this.userRepository = userRepository;
        this.filialRepository = filialRepository;
        this.motoRepository = motoRepository;
    }

    public List<KpiChartDto> getRegistrationsLast6Months() {
        return userRepository.findRegistrationsPerMonth();
    }

    public ChartData getRegistrationChartData() {
        Map<String, Integer> monthlyData = new LinkedHashMap<>();
        YearMonth startMonth = YearMonth.now().minusMonths(5);
        Locale ptBR = new Locale("pt", "BR");

        for (int i = 0; i < 6; i++) {
            YearMonth currentMonth = startMonth.plusMonths(i);
            String key = currentMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyData.put(key, 0);
        }
        List<KpiChartDto> dbResults = userRepository.findRegistrationsPerMonth();
        for (KpiChartDto dto : dbResults) {
            String dbKey = dto.getLabel();
            Integer dbValue = dto.getValue().intValue();

            if (monthlyData.containsKey(dbKey)) {
                monthlyData.put(dbKey, dbValue);
            }
        }

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM", ptBR);

        for (Map.Entry<String, Integer> entry : monthlyData.entrySet()) {
            YearMonth ym = YearMonth.parse(entry.getKey());
            String formattedLabel = labelFormatter.format(ym).replace(".", "");
            labels.add(StringUtils.capitalize(formattedLabel));
            data.add(entry.getValue());
        }

        return new ChartData(labels, data);
    }

    public ChartData getActivesByModel(){
        Map<String, Integer> modelData = new LinkedHashMap<>();

        modelData.put("Mottu-E", 0);
        modelData.put("Mottu Pop", 0);
        modelData.put("Mottu Sport", 0);

        var dbResults = motoRepository.findActivesPerModel();

        for (KpiChartDto dto: dbResults){
            String modelName = dto.getLabel();
            if(modelData.containsKey(modelName)){
                modelData.put(modelName, dto.getValue().intValue());
            }
        }
        List<String> labels = new ArrayList<>(modelData.keySet());
        List<Integer> data = new ArrayList<>(modelData.values());
        return new ChartData(labels, data);
    }
}
