package ru.maslennikov.thirdProject.SensorApp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
import ru.maslennikov.thirdProject.SensorApp.services.SensorService;

@Component
public class MeasurementValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (sensorService.findByName(((Measurement) target).getSensor().getName()).isEmpty()){
            errors.rejectValue("sensor", "sensor name not found");
        }
        if (((Measurement) target).getSensor() == null) {
            errors.rejectValue("sensor", "enter sensor name");
        }
    }
}
