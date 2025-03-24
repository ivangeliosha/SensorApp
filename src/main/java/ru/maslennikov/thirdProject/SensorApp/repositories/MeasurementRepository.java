package ru.maslennikov.thirdProject.SensorApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    int countByRainingTrue();
}
