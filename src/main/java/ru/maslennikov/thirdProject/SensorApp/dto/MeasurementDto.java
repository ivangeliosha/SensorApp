package ru.maslennikov.thirdProject.SensorApp.dto;

import org.hibernate.validator.constraints.Range;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;

import javax.validation.constraints.NotNull;

public class MeasurementDto {

    @NotNull(message = "Value cannot be null")
    @Range(min = -100, max = 100, message = "Value must be between -100 and 100")
    private Float value;

    @NotNull(message = "Is it raining? Cannot be null")
    private Boolean raining; // Используем объект Boolean вместо примитива

    @NotNull(message = "Sensor cannot be null")
    private Sensor sensor;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public @NotNull(message = "Sensor cannot be null") Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull(message = "Sensor cannot be null") Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "MeasurementDto{" +
                "value=" + value +
                ", raining=" + raining +
                ", sensor=" + sensor +
                '}';
    }
}
