package ru.maslennikov.thirdProject.SensorApp.client;


import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.concurrent.atomic.AtomicInteger;

public class ScannerPost {
    public static void main(String[] args) {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader("Content-Type", "application/json")
            .build();

        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger errorCounter = new AtomicInteger(0);

        Flux.range(1, 1000)
            .flatMap(i -> {
                // Генерация данных с учётом ограничений DTO
                String name = "sensor" + i;

                // Формируем JSON строго по структуре MeasurementDto
                String json = String.format(
                    "{\"name\":\"%s\"}",
                    name
                );
                System.out.println(json);

                return webClient.post()
                    .uri("/sensors/registration")
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
