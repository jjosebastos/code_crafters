package com.br.code_crafters.forms.monitoring;

import com.br.code_crafters.forms.moto.Moto;
import com.br.code_crafters.forms.moto.MotoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MonitoramentoService {

    private final MotoRepository motoRepository;

    public MonitoramentoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    public int countOccupied(UUID idPatio) {
        List<Moto> motos = motoRepository.findByPatio_IdPatioOrderByNrPlacaAsc(idPatio);
        return motos.size();
    }

    public List<List<CellDto>> buildGrid(UUID idPatio, int cols, int totalSlotsProvided) {
        List<Moto> motos = motoRepository.findByPatio_IdPatioOrderByNrPlacaAsc(idPatio);


        int motosCount = motos.size();
        int totalSlots = totalSlotsProvided > 0 ? totalSlotsProvided : Math.max(motosCount + 5, 20);
        if (totalSlots < motosCount) totalSlots = motosCount; // garantia

        int rows = (int) Math.ceil((double) totalSlots / cols);

        List<CellDto> allCells = new ArrayList<>(totalSlots);
        for (int r = 0; r < rows; r++) {
            char rowLetter = (char) ('A' + r);
            for (int c = 1; c <= cols; c++) {
                String label = rowLetter + String.valueOf(c);
                allCells.add(new CellDto(label));
                if (allCells.size() == totalSlots) break;
            }
            if (allCells.size() == totalSlots) break;
        }

        for (int i = 0; i < motosCount && i < allCells.size(); i++) {
            Moto m = motos.get(i);
            MotoSimpleDto ms = new MotoSimpleDto(m.getNrPlaca(), m.getNmModelo(), m.getFlStatus());
            CellDto filled = new CellDto(allCells.get(i).getSlotLabel(), ms);
            allCells.set(i, filled);
        }


        List<List<CellDto>> grid = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            int start = r * cols;
            int end = Math.min(start + cols, allCells.size());
            grid.add(new ArrayList<>(allCells.subList(start, end)));
        }

        return grid;
    }


}
