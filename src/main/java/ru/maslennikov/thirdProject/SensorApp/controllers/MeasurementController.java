package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementDto;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
import ru.maslennikov.thirdProject.SensorApp.services.MeasurementService;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
import ru.maslennikov.thirdProject.SensorApp.util.MeasurementErrorResponse;
import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedMeasurementException;

import javax.validation.Valid;
import java.util.List;
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
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDto measurementDto,
                                                     BindingResult bindingResult) throws NotCreatedMeasurementException {
        if (bindingResult.hasErrors()) {
            StringBuilder measurementsErrors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> measurementsErrors.append(error.getDefaultMessage()));
            throw new NotCreatedMeasurementException(measurementsErrors.toString());
        }

        measurementService.save(convertToMeasurement(measurementDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(NotCreatedMeasurementException.class)
    public ResponseEntity<MeasurementErrorResponse> handleException(NotCreatedMeasurementException e) {
    MeasurementErrorResponse response = new MeasurementErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );//csdfsdf

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    private MeasurementDto convertToMeasurementDto(Measurement measurement){
        MeasurementDto measurementDto = modelMapper.map(measurement, MeasurementDto.class);
        measurementDto.setSensor(measurement.getSensor());
        return measurementDto;
    }
    public Measurement convertToMeasurement(MeasurementDto measurementDTO) throws NotCreatedMeasurementException {
    Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
    measurement.setSensor(sensorService.findByName(measurementDTO.getSensor().getName())
        .orElseThrow(() -> new NotCreatedMeasurementException("Error")));
    return measurement;
    }
}
