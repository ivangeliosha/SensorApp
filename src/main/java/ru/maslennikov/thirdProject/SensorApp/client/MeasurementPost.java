package ru.maslennikov.thirdProject.SensorApp.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class MeasurementPost {
    public static void main(String[] args) {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader("Content-Type", "application/json")
            .build();

        Random random = new Random();
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger errorCounter = new AtomicInteger(0);


        Flux.range(1, 1000)
            .flatMap(i -> {
                // Устанавливаем локаль, где десятичный разделитель - точка (для String.format)
                Locale.setDefault(Locale.US);
                // Имя сенсора должно быть зарегистрировано в системе
                String sensorName = "sensor" + i;

                // Генерация данных с учётом ограничений DTO
                Float value = Math.round((-100 + 200 * random.nextFloat()) * 100.0f) / 100.0f;
                Boolean raining = random.nextBoolean();

                // Формируем JSON строго по структуре MeasurementDto
                String json = String.format(
                    "{\"value\":%.2f,\"raining\":%s,\"sensor\":{\"name\":\"%s\"}}",
                    value, raining, sensorName
                );
                System.out.println(json);

                return webClient.post()
                    .uri("/measurements/add")
                    .bodyValue(json)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> {
                        System.out.printf("[Успех #%d] Отправлено: %s%n",
                            successCounter.incrementAndGet(), json);
                    })
                    .onErrorResume(e -> {
                        System.err.printf("[Ошибка #%d] %s | Запрос: %s%n",
                            errorCounter.incrementAndGet(),
                            e.getMessage(), json);
                        return Mono.empty();
                    });
            })
            .blockLast();

        System.out.printf("%nИтог:%nУспешно: %d%nОшибок: %d%n",
            successCounter.get(), errorCounter.get());
    }
}