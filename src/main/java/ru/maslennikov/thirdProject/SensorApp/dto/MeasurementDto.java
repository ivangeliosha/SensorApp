package ru.maslennikov.thirdProject.SensorApp.dto;

import org.hibernate.validator.constraints.Range;
import ru.maslennikov.thirdProject.SensorApp.models.Sensor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MeasurementDto {

    @Range(min = -100, max = 100)
    @NotEmpty(message = "please write the value")
    private float value;

    @NotNull(message = "Is it raining?")
    private boolean raining;

    @NotNull(message = "Sensor cannot be null")
    private Sensor sensor;

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

    public Sensor getSensor() {
            return sensor;
        }

    public void setSensor(Sensor sensor) {
            this.sensor = sensor;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementDto that = (MeasurementDto) o;
        return Float.compare(value, that.value) == 0 && raining == that.raining && Objects.equals(sensor, that.sensor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, raining, sensor);
    }
}
