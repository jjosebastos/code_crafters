package com.br.code_crafters.forms.moto;

import com.br.code_crafters.forms.sensor.Sensor;
import com.br.code_crafters.forms.sensor.SensorDto;
import com.br.code_crafters.forms.sensor.SensorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final SensorRepository sensorRepository;


    public MotoService(MotoRepository motoRepository, SensorRepository sensorRepository) {
        this.motoRepository = motoRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public Moto saveMoto(MotoDto dto) {
        Moto moto = motoMapper(dto);
        Moto savedMoto = motoRepository.save(moto);

        if (dto.getSensor() != null) {
            Sensor s = sensorMapper(dto.getSensor());
            s.setMoto(savedMoto);
            if (s.getDtInstalacao() == null) {
                s.setDtInstalacao(OffsetDateTime.now(ZoneOffset.UTC));
            }
            sensorRepository.save(s);
        }

        return savedMoto;
    }


    public void deleteById(UUID idMoto){
        motoRepository.deleteById(idMoto);
    }

    public List<Moto> findAll(){
        return motoRepository.findAll();
    }

    private Moto motoMapper(MotoDto input){
        return Moto.builder()
                .nmModelo(input.getModelo())
                .nrChassi(input.getChassi())
                .nrPlaca(input.getPlaca())
                .flStatus(input.getStatus())
                .idOperador(input.getIdOperador())
                .idPatio(input.getIdPatio())
                .build();
    }

    private Sensor sensorMapper(SensorDto in) {
        return Sensor.builder()
                .nmModelo(in.getModelo())
                .tpSensor(in.getTipo())
                .nmFabricante(in.getFabricante())
                .vsFirmware(in.getFirmware())
                .dtCalibracao(in.getDataCalibracao() != null ? in.getDataCalibracao() : null)
                .build();
    }
}
