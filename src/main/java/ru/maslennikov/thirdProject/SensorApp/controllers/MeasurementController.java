    package ru.maslennikov.thirdProject.SensorApp.controllers;

    import org.modelmapper.ModelMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.DataIntegrityViolationException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;
    import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementDto;
    import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
    import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
    import ru.maslennikov.thirdProject.SensorApp.services.MeasurementService;
    import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
    import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;

    import javax.validation.Valid;
    import java.sql.SQLException;
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
    public ResponseEntity<HttpStatus> addMeasurement(
            @Valid @RequestBody MeasurementDto measurementDto,
            BindingResult bindingResult) throws NotCreatedException {

        // Проверка на ошибки валидации
        SensorController.findErrors(bindingResult);
        try {
            // Сохранение измерения
            measurementService.save(convertToMeasurement(measurementDto));
        } catch (DataIntegrityViolationException e) {
            // Обработка ошибок базы данных (например, нарушение ограничений уникальности или not-null)
            String errorMessage = "Database constraint violation occurred,correct your request. ";//Произошло нарушение ограничений базы данных.

            // Проверяем наличие корневого исключения SQLException
            if (e.getCause() instanceof SQLException) {
                SQLException sqlException = (SQLException) e.getCause();
                // Выводим код ошибки или дополнительную информацию из SQLException
                errorMessage += "SQLState: " + sqlException.getSQLState() + ", ErrorCode: "+ sqlException.getErrorCode();
            }

            // Вставляем дополнительные детали ошибки
            errorMessage += "Error details: " + e.getMessage();

            throw new NotCreatedException(errorMessage);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }




    public Measurement convertToMeasurement(MeasurementDto measurementDTO) throws NotCreatedException {

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


    private MeasurementDto convertToMeasurementDto(Measurement measurement) {
        MeasurementDto measurementDto = modelMapper.map(measurement, MeasurementDto.class);
        Sensor sensor = measurement.getSensor();
        measurementDto.setSensor(sensor);
        return measurementDto;
    }
}
