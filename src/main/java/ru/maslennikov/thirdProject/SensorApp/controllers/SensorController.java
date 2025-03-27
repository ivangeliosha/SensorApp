package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.maslennikov.thirdProject.SensorApp.dto.SensorDto;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;

import javax.validation.Valid;
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

        if (bindingResult.hasErrors()) {
            throw new NotCreatedException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        sensorService.save(convertToSensor(sensorDto));//todo + bindingResults
        return ResponseEntity.ok(HttpStatus.OK);
    }



    @ExceptionHandler()//todo
    private ResponseEntity<HttpStatus> handleException(Exception ex, WebRequest request) {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private SensorDto convertToSensorDto(Sensor sensor){
        return modelMapper.map(sensor, SensorDto.class);
    }
    private Sensor convertToSensor(SensorDto sensorDto){
        return modelMapper.map(sensorDto, Sensor.class);
    }

}
