    package ru.maslennikov.thirdProject.SensorApp.controllers;

    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementDTO;
    import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementResponse;
    import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
    import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
    import ru.maslennikov.thirdProject.SensorApp.services.MeasurementService;
    import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
    import ru.maslennikov.thirdProject.SensorApp.util.MeasurementValidator;
    import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;

    import javax.validation.Valid;
    import java.util.stream.Collectors;


    @RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService,
                                 ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;

    }

    @GetMapping()
    public MeasurementResponse allMeasurements() {
        return new MeasurementResponse(measurementService.findAll().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public int rainyDaysCount() {
        return measurementService.findRainingCount();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(
            @Valid @RequestBody MeasurementDTO measurementDTO,
            BindingResult bindingResult) throws NotCreatedException {

        Measurement measurementToSave = convertToMeasurement(measurementDTO);

        measurementValidator.validate(measurementToSave, bindingResult);
        if (bindingResult.hasErrors())
            throw new NotCreatedException(bindingResult.getAllErrors().get(0).getDefaultMessage());

        // Сохранение измерения
        measurementService.save(measurementToSave);

        return new ResponseEntity<>(HttpStatus.OK);
    }




    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) throws NotCreatedException {

    if (measurementDTO == null || measurementDTO.getSensor() == null || measurementDTO.getSensor().getName() == null) {
        throw new NotCreatedException("Sensor name must be provided or I cannot find him.");
    }

    Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);

    try {
        Sensor sensor = sensorService.findByName(measurementDTO.getSensor().getName())
                .orElseThrow(() -> new NotCreatedException("Sensor with name '"
                        + measurementDTO.getSensor().getName() + "' not found"));
        measurement.setSensor(sensor);
    } catch (Exception e) {
        throw new NotCreatedException("Error while fetching sensor data: " + e.getMessage());
    }

    return measurement;
    }


    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = modelMapper.map(measurement, MeasurementDTO.class);
        Sensor sensor = measurement.getSensor();
        measurementDTO.setSensor(sensor);
        return measurementDTO;
    }
}
