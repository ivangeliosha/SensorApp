package ru.maslennikov.thirdProject.SensorApp.dto;

import org.hibernate.validator.constraints.Range;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class MeasurementDto {

    @Range(min = -100, max = 100)
    private float value;

    @NotNull(message = "Is it raining?")
    private boolean raining;

    //private Sensor sensor;

    private String sensorName;

    @Range(min = -100, max = 100)
    public float getValue() {
        return value;
    }

    public void setValue(@Range(min = -100, max = 100) float value) {
        this.value = value;
    }

    @NotNull(message = "Is it raining?")
    public boolean isRaining() {
        return raining;
    }

    public void setRaining(@NotNull(message = "Is it raining?") boolean raining) {
        this.raining = raining;
    }

    //public Sensor getSensor() {
    //        return sensor;
    //    }
    //
    //public void setSensor(Sensor sensor) {
    //        this.sensor = sensor;
    //    }
    //
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
}
