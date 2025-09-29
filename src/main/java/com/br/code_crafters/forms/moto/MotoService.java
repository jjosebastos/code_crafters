package com.br.code_crafters.forms.moto;

import com.br.code_crafters.forms.operador.Operador;
import com.br.code_crafters.forms.operador.OperadorRepository;
import com.br.code_crafters.forms.patio.Patio;
import com.br.code_crafters.forms.patio.PatioRepository;
import com.br.code_crafters.forms.sensor.Sensor;
import com.br.code_crafters.forms.sensor.SensorDto;
import com.br.code_crafters.forms.sensor.SensorRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final OperadorRepository operadorRepository;
    private final SensorRepository sensorRepository;
    private final PatioRepository patioRepository;


    public MotoService(MotoRepository motoRepository,
                       OperadorRepository operadorRepository,
                       SensorRepository sensorRepository,
                       PatioRepository patioRepository) {
        this.motoRepository = motoRepository;
        this.operadorRepository = operadorRepository;
        this.sensorRepository = sensorRepository;
        this.patioRepository = patioRepository;
    }

    @Transactional
    public Moto saveMoto(MotoDto dto) {
        Moto moto = motoMapper(dto);
        Moto savedMoto = motoRepository.save(moto);

        if (dto.getSensor() != null) {
            Sensor s = sensorMapper(dto.getSensor());
            s.setMoto(savedMoto);
            sensorRepository.save(s);
        }

        return savedMoto;
    }


    @Transactional
    public void update(UUID uuid, MotoDto dto){
        var found = motoRepository.findById(uuid).orElseThrow(() ->
                new NoSuchElementException("Moto não encontrada"));
;
        var mapperMoto = updateMotoMapper(found, dto);
        motoRepository.save(mapperMoto);
    }


    @Transactional
    public void deleteById(UUID idMoto){
        motoRepository.deleteById(idMoto);
    }

    public Page<Moto> findAll(Pageable pageable){
        return motoRepository.findAll(pageable);
    }

    public Optional<Moto> findById(UUID uuid){
        return motoRepository.findById(uuid);
    }

    public List<MotoSensorDto> findMotoSensorByPlaca(String nrPlaca){
        return motoRepository.findMotoSensorByPlacaOrModelo(nrPlaca);
    }



    private Moto motoMapper(MotoDto input){
        Patio patio = null;
        Operador operador = null;
        if(input.getIdPatio() != null){
            Optional<Patio> optionalPatio = patioRepository.findById(input.getIdPatio());
            patio = optionalPatio.orElseThrow(() -> new NoSuchElementException("Patio não econtrado"));
        }

        if(input.getIdOperador() != null){
            Optional<Operador> optionalOperador = operadorRepository.findById(input.getIdOperador());
            operador = optionalOperador.orElse(null);
        }

        return Moto.builder()
                .idMoto(input.getIdMoto())
                .nmModelo(input.getNmModelo())
                .nrChassi(input.getNrChassi())
                .nrPlaca(input.getNrPlaca())
                .patio(patio)
                .flStatus(input.getFlStatus())
                .operador(operador)
                .build();
    }

    private Moto updateMotoMapper(Moto existingMoto, MotoDto input) {
        existingMoto.setNmModelo(input.getNmModelo());
        existingMoto.setNrChassi(input.getNrChassi());
        existingMoto.setNrPlaca(input.getNrPlaca());

        if (input.getIdPatio() != null) {
            Patio patio = patioRepository.findById(input.getIdPatio())
                    .orElseThrow(() -> new NoSuchElementException("Pátio não encontrado."));
            existingMoto.setPatio(patio);
        } else {
            existingMoto.setPatio(null);
        }
        if (input.getIdOperador() != null) {
            Operador operador = operadorRepository.findById(input.getIdOperador())
                    .orElse(null);
            existingMoto.setOperador(operador);
        } else {
            existingMoto.setOperador(null);
        }

        return existingMoto;
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
