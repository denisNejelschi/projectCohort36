package gr36.clubActiv.exeption_handling;

import java.util.Objects;

public class Response {

  private final String message;
  private String additionalMessage;

  public Response(String message) {
    this.message = message;
  }

  public Response(String message, String additionalMessage) {
    this.message = message;
    this.additionalMessage = additionalMessage;
  }

  public String getMessage() {
    return message;
  }

  public String getAdditionalMessage() {
    return additionalMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Response response)) return false;
    return Objects.equals(message, response.message);
  }

  @Override
  public String toString() {
    return String.format("Response: message - %s%s",
        message,
        additionalMessage == null ? "" : ", additionalMessage: " + additionalMessage);
  }
}