package gr36.clubActiv.exeption_handling.exeptions;

public class ConfirmationFailedException extends RuntimeException{
  public ConfirmationFailedException(String message) {
    super(message);
  }
}
