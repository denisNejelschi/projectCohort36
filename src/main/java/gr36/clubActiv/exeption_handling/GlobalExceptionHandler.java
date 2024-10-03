package gr36.clubActiv.exeption_handling;

import gr36.clubActiv.exeption_handling.exeptions.ActivityNotFoundException;
import gr36.clubActiv.exeption_handling.exeptions.ConfirmationFailedException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ActivityNotFoundException.class)
  public ResponseEntity<Response> handleException(ActivityNotFoundException e) {
    log.error("ActivityNotFoundException occurred: {}", e.getMessage());

    Response response = new Response(
        e.getMessage(),
        "Activity not found", // Дополнительное сообщение
        HttpStatus.NOT_FOUND.value() // Статус 404
    );

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Response> handleException(UserNotFoundException e) {
    log.error("UserNotFoundException occurred: {}", e.getMessage());

    Response response = new Response(
        e.getMessage(),
        "Activity not found", // Дополнительное сообщение
        HttpStatus.NOT_FOUND.value() // Статус 404
    );

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(ConfirmationFailedException.class)
  public ResponseEntity<Response> handleException(ConfirmationFailedException e) {
    Response response = new Response(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}



// 3 способ обработки ошибок
// ПЛЮС - у нас есть глобальный обработчик ошибок, который
// позволяет перехватить исключения, возникающие в любом
// месте проекта, при этом отправив клиенту нужное сообщение
// вместе с нужным http-статусом
// ПЛЮС - всю логику обработки ошибок мы выносим в отдельное место
// проекта (то есть в этот класс), тем самым мы не загромождаем
// основную логику логикой обработки ошибок. Вся логика обработки
// ошибок у нас сконцентрирована в одном месте, что облегчает её доработку.
// МИНУС - при данном подходе мы не можем прописать какую-то специфичную
// логику обработки ошибок под какой-то конкретный контроллер.
