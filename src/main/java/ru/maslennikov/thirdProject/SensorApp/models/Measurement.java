package ru.maslennikov.thirdProject.SensorApp.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity()
@Table(name = "Measurement")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    @Range(min = -100, max = 100)

    @NotNull(message = "please write the value")
    private float value;

    @Column(name = "raining")
    @NotNull(message = "Is it raining?")
    private Boolean raining;

    @Column(name = "measure_time")
    @NotNull
    private LocalDateTime measureTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @NotNull(message = "please write sensor name")
    private Sensor sensor;

    public Measurement() {
    }

    public Measurement(float value, boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public @NotNull LocalDateTime getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(@NotNull LocalDateTime measureTime) {
        this.measureTime = measureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return id == that.id && Float.compare(value, that.value) == 0 && raining == that.raining && Objects.equals(measureTime, that.measureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, raining, measureTime);
    }
}
