package com.br.code_crafters.forms.monitoring;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CellDto {
    private String slotLabel;
    private boolean occupied;
    private MotoSimpleDto moto;

    public CellDto() {}

    public CellDto(String slotLabel) {
        this.slotLabel = slotLabel;
        this.occupied = false;
    }

    // CONSTRUTOR ADICIONADO
    public CellDto(String slotLabel, MotoSimpleDto moto) {
        this.slotLabel = slotLabel;
        this.moto = moto;
        this.occupied = moto != null;
    }

    // getters e setters
    public String getSlotLabel() { return slotLabel; }
    public void setSlotLabel(String slotLabel) { this.slotLabel = slotLabel; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public MotoSimpleDto getMoto() { return moto; }
    public void setMoto(MotoSimpleDto moto) { this.moto = moto; }
}
