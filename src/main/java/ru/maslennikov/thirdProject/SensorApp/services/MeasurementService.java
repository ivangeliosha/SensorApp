package ru.maslennikov.thirdProject.SensorApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.maslennikov.thirdProject.SensorApp.dto.MeasurementDto;
import ru.maslennikov.thirdProject.SensorApp.models.Measurement;
import ru.maslennikov.thirdProject.SensorApp.repositories.MeasurementRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
