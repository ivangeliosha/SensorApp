package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maslennikov.thirdProject.SensorApp.dto.SensorDto;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<SensorDto> getSensors() {
        return sensorService.findAll()
                .stream().map(this::convertToSensorDto).collect(Collectors.toList());
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> addSensor(@RequestBody @Valid SensorDto sensorDto,
                                                BindingResult bindingResult) throws NotCreatedException {

        findErrors(bindingResult);
        try {
            // Сохранение
            sensorService.save(convertToSensor(sensorDto));
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
    @DeleteMapping("/{name}")
    public ResponseEntity<HttpStatus> deleteSensor(@PathVariable("name") String name)
            throws ChangeSetPersister.NotFoundException {
        try {
            sensorService.deleteByName(name);
        }catch (DataIntegrityViolationException e) {
            throw new ChangeSetPersister.NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    static void findErrors(BindingResult bindingResult) throws NotCreatedException {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append("; ");
            }

            if (errorMsg.length() > 0) {
                throw new NotCreatedException(errorMsg.toString());
            }
        }
    }

    private SensorDto convertToSensorDto(Sensor sensor){
        return modelMapper.map(sensor, SensorDto.class);
    }

    private Sensor convertToSensor(SensorDto sensorDto) throws NotCreatedException {

    if (sensorDto == null || sensorDto.getName() == null) {
        throw new NotCreatedException("Sensor name must be provided or I cannot find him.");
    }


        return modelMapper.map(sensorDto, Sensor.class);
    }

}
