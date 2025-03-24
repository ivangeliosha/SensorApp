package ru.maslennikov.thirdProject.SensorApp.models;

import org.hibernate.annotations.Cascade;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Alishev
 */
@Entity
@Table(name = "Sensor")
public class Sensor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 3, max = 30,message = "Size should be between 3 and 30 characters")
    private String name;

    @OneToMany(mappedBy = "sensor",cascade = CascadeType.ALL)
    private List<Measurement> measurementList = new ArrayList<>();

    public Sensor() {

    }

    public Sensor(String name, int age) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
