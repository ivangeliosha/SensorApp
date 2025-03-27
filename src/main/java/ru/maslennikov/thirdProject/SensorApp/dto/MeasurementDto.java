package ru.maslennikov.thirdProject.SensorApp.dto;


import ru.maslennikov.thirdProject.SensorApp.models.Sensor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class MeasurementDto {

    @NotNull(message = "Value cannot be null")
    @DecimalMin(value = "-100.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
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
