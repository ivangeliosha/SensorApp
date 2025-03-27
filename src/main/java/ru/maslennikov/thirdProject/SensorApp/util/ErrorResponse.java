package ru.maslennikov.thirdProject.SensorApp.util;

import java.time.Instant;

public class ErrorResponse {
    private String message;
    private String timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = Instant.now().toString(); // ISO 8601 формат времени
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
