package gr36.clubActiv.exeption_handling;

import java.time.LocalDateTime;
import java.util.Objects;

public class Response {

  private final String message;
//  private String additionalMessage;
  private int status;
  private final LocalDateTime timestamp;

  // Конструктор для минимальной информации (только сообщение)
  public Response(String message) {
    this.message = message;
    this.timestamp = LocalDateTime.now(); // По умолчанию устанавливаем текущее время
  }

  // Конструктор с сообщением, дополнительным сообщением, статусом и временной меткой
  public Response(String message, String additionalMessage, int status) {
    this.message = message;
//    this.additionalMessage = additionalMessage;
    this.status = status;
    this.timestamp = LocalDateTime.now(); // Устанавливаем текущее время
  }

  // Геттеры для полей
  public String getMessage() {
    return message;
  }

//  public String getAdditionalMessage() {
//    return additionalMessage;
//  }

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
//        Objects.equals(additionalMessage, response.additionalMessage) &&
        Objects.equals(status, response.status) &&
        Objects.equals(timestamp, response.timestamp);
  }

  @Override
  public String toString() {
    return String.format("Response: status - %d, message - %s, "
//            + "additionalMessage: %s, "
            + "timestamp: %s",
        status,
        message,
//        additionalMessage == null ? "N/A" : additionalMessage,
        timestamp.toString());
  }
}
