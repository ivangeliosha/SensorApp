package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementDto;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
import ru.maslennikov.thirdProject.SensorApp.services.MeasurementService;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MeasurementDto> allMeasurements() {
        return measurementService.findAll()
                .stream().map(this::convertToMeasurementDto).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public int rainyDaysCount() {
        return measurementService.findRainingCount();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDto measurementDto) {

        measurementService.save(convertToMeasurement(measurementDto));

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private MeasurementDto convertToMeasurementDto(Measurement measurement){
        MeasurementDto measurementDto = modelMapper.map(measurement, MeasurementDto.class);
        measurementDto.setSensorName(measurement.getSensor().getName());
        return measurementDto;
    }
    public Measurement convertToMeasurement(MeasurementDto measurementDTO){
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        Optional<Sensor> sensor = sensorService.findByName(measurementDTO.getSensorName());
        measurement.setSensor(sensor.get());
        return measurement;
    }
}
