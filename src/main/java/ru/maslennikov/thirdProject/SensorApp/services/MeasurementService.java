package ru.maslennikov.thirdProject.SensorApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
import ru.maslennikov.thirdProject.SensorApp.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void save(Measurement measurement) {
        measurement.setMeasureTime(LocalDateTime.now());
        measurementRepository.save(measurement);
    }

    public int findRainingCount() {
        return measurementRepository.countByRainingTrue();
    }



}
