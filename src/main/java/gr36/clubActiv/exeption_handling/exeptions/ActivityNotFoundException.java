package gr36.clubActiv.exeption_handling.exeptions;

public class ActivityNotFoundException extends RuntimeException {

  public ActivityNotFoundException(Long activity_id) {
    super(String.format("Activity with id %d not found", activity_id));
  }

  public ActivityNotFoundException(String title) {
    super(String.format("Activity with title %s not found", title));
  }

//  public ActivityNotFoundException(Long userId, Long activityId) {
//    super(String.format("Activity with id %d not found for user %d", activityId, userId));
//  }
}


//    try {
//      log.info("Fetching activity by ID: {}", id);
//      ActivityDto activity = service.getActivityById(id);
//      return ResponseEntity.ok(activity);
//    } catch (EntityNotFoundException e) {
//      log.error("Activity not found with ID: {}", id, e);
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    } catch (Exception e) {
//      log.error("Error while fetching activity by ID: {}", id, e);
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }