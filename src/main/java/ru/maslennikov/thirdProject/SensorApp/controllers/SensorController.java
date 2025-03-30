package ru.maslennikov.thirdProject.SensorApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maslennikov.thirdProject.SensorApp.dto.SensorDTO;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;
import ru.maslennikov.thirdProject.SensorApp.util.NotCreatedException;
import ru.maslennikov.thirdProject.SensorApp.util.NotFoundException;
import ru.maslennikov.thirdProject.SensorApp.util.SensorValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @GetMapping()
    public List<SensorDTO> getSensors() {
        return sensorService.findAll()
                .stream().map(this::convertToSensorDTO).collect(Collectors.toList());
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> addSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                BindingResult bindingResult) throws NotCreatedException {

        Sensor sensorToSave = convertToSensor(sensorDTO);

        sensorValidator.validate(sensorToSave, bindingResult);
        if (bindingResult.hasErrors())
            throw new NotCreatedException(bindingResult.getAllErrors().get(0).getDefaultMessage());

        sensorService.save(sensorToSave);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/{name}")
    public ResponseEntity<HttpStatus> deleteSensor(@PathVariable("name") String name)
            throws NotFoundException {
        if (name.isEmpty() || sensorService.findByName(name).isEmpty()) {
            throw new NotFoundException(name);
        }
        try {
            sensorService.deleteByName(name);
        }catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor){
        return modelMapper.map(sensor, SensorDTO.class);
    }

    private Sensor convertToSensor(@Valid SensorDTO sensorDTO) throws NotCreatedException {
        try {
            return modelMapper.map(sensorDTO, Sensor.class);
        }catch (DataIntegrityViolationException e){
            throw new NotCreatedException(e.getMessage());
        }

    }

}
