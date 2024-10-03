package gr36.clubActiv.exeption_handling;

import java.time.LocalDateTime;
import java.util.Objects;

public class Response {

  private final String message;
  private final int status;
  private final LocalDateTime timestamp;

  // Конструктор для минимальной информации (только сообщение)
  public Response(String message) {
    this.message = message;
    this.status = 400; // Задайте статус по умолчанию (например, 400 - Bad Request)
    this.timestamp = LocalDateTime.now(); // Устанавливаем текущее время
  }

  // Конструктор с сообщением, статусом и временной меткой
  public Response(String message, int status) {
    this.message = message;
    this.status = status;
    this.timestamp = LocalDateTime.now(); // Устанавливаем текущее время
  }

  // Геттеры
  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Response response)) return false;
    return Objects.equals(message, response.message) &&
        Objects.equals(status, response.status) &&
        Objects.equals(timestamp, response.timestamp);
  }

  @Override
  public String toString() {
    return String.format("Response: status - %d, message - %s, timestamp: %s",
        status, message, timestamp.toString());
  }
}
