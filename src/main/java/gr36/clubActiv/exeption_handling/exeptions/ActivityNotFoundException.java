package gr36.clubActiv.exeption_handling.exeptions;

public class ActivityNotFoundException extends RuntimeException {

  public ActivityNotFoundException(Long activity_id) {
    super(String.format("Activity with id %d not found", activity_id));
  }

  public ActivityNotFoundException(String title) {
    super(String.format("Activity with title %s not found", title));
  }
}
