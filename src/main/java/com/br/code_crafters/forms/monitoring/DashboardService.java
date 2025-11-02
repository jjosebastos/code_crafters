package com.br.code_crafters.forms.monitoring;

import com.br.code_crafters.forms.filial.FilialRepository;
import com.br.code_crafters.forms.moto.MotoRepository;
import com.br.code_crafters.forms.patio.PatioRepository;
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
    private final PatioRepository patioRepository;

    public DashboardService(UserRepository userRepository, FilialRepository filialRepository, MotoRepository motoRepository, PatioRepository patioRepository) {
        this.userRepository = userRepository;
        this.filialRepository = filialRepository;
        this.motoRepository = motoRepository;
        this.patioRepository = patioRepository;
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
        modelData.put("MOTTU-E", 0);
        modelData.put("MOTTU POP", 0);
        modelData.put("MOTTU SPORT", 0);
        var dbResults = motoRepository.findActivesPerModel();

        for (KpiChartDto dto: dbResults){
            String modelName = dto.getLabel();
            Long value = dto.getValue();
            if (modelName != null && modelData.containsKey(modelName)) {
                modelData.put(modelName, (value != null) ? value.intValue() : 0);
            }
        }

        List<String> labels = new ArrayList<>(modelData.keySet());
        List<Integer> data = new ArrayList<>(modelData.values());

        return new ChartData(labels, data);
    }

    public ChartData getFiliaisChartData(){
        Map<String, Integer> modelData = new LinkedHashMap<>();
        var dbResults = filialRepository.getFilialCountByUf();
        for(KpiChartDto chart : dbResults){
            String ufName = chart.getLabel();
            modelData.put(ufName, chart.getValue().intValue());
        }
        List<String> labels = new ArrayList<>(modelData.keySet());
        List<Integer> data = new ArrayList<>(modelData.values());
        return new ChartData(labels, data);
    }

    public PatioChartData getPatiosChartData() {

        Map<String, Integer> patiosDataMap = new LinkedHashMap<>();

        var dbResults = patioRepository.getPatiosChartData();

        for (KpiChartDto dto : dbResults) {
            String patioLabel = dto.getLabel();
            Integer count = (dto.getValue() != null) ? dto.getValue().intValue() : 0;
            patiosDataMap.put(patioLabel, count);
        }
        List<String> labels = new ArrayList<>(patiosDataMap.keySet());
        List<Integer> data = new ArrayList<>(patiosDataMap.values());

        Integer motosNoPatio = patiosDataMap.getOrDefault("No Pátio", 0);
        Integer motosForaDoPatio = patiosDataMap.getOrDefault("Fora do Pátio", 0);

        return new PatioChartData(labels, data, motosNoPatio, motosForaDoPatio);
    }
    public long countFiliais(){
        return filialRepository.count();
    }
}
