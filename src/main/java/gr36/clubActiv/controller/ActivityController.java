package gr36.clubActiv.controller;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.services.interfaces.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityService service;
  private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

  public ActivityController(ActivityService service) {
    this.service = service;
  }

  @GetMapping("/test")
  public String test() {
    log.info("Test endpoint called");
    return "Hello World";
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody ActivityDto activity) {
    try {
      log.info("Creating new activity: {}", activity);
      ActivityDto createdActivity = service.create(activity);
      log.info("Activity created successfully with ID: {}", createdActivity.getId());
      return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
    } catch (Exception e) {
      log.error("Error while creating activity", e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create activity");
    }
  }

  @GetMapping
  public List<ActivityDto> getAllActivities() {
    log.info("Fetching all activities");
    return service.getAllActivities();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id) {
    try {
      log.info("Fetching activity by ID: {}", id);
      ActivityDto activity = service.getActivityById(id);
      return ResponseEntity.ok(activity);
    } catch (EntityNotFoundException e) {
      log.error("Activity not found with ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      log.error("Error while fetching activity by ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(
      @PathVariable("id") Long id,
      @RequestBody ActivityDto dto) {
    try {
      log.info("Updating activity with ID: {}", id);
      ActivityDto updatedActivity = service.update(id, dto);
      return ResponseEntity.ok(updatedActivity);
    } catch (EntityNotFoundException e) {
      log.error("Activity not found with ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found");
    } catch (Exception e) {
      log.error("Error while updating activity with ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update activity");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
    try {
      log.info("Deleting activity with ID: {}", id);
      service.deleteActivity(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      log.error("Activity not found with ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activity not found");
    } catch (Exception e) {
      log.error("Error while deleting activity with ID: {}", id, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete activity");
    }
  }

  @PostMapping("/{activity_id}/add-user/{user_id}") //TODO POST -> PUT
  public ResponseEntity<ActivityDto> addUserToActivity(@PathVariable Long activity_id, @PathVariable Long user_id) {
    ActivityDto updatedActivity = service.addUserToActivity(activity_id, user_id);
    return ResponseEntity.ok(updatedActivity); // Возвращаем обновленную активность
  }

  @GetMapping("/user/{userId}/activities")
  public List<ActivityDto> getActivitiesByUserId(@PathVariable Long userId) {
    log.info("Fetching activities for user ID: {}", userId); // Логирование
    return service.getActivitiesByUserId(userId);
  }

  @DeleteMapping("/{activity_id}/remove-user/{user_id}")
  public ResponseEntity<?> removeUserFromActivity(@PathVariable Long activity_id, @PathVariable Long user_id) {
    try {
      ActivityDto updatedActivity = service.removeUserFromActivity(activity_id, user_id);
      return ResponseEntity.ok(updatedActivity);
    } catch (IllegalArgumentException e) {
      // Handle invalid activity_id or user_id
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      // Handle other exceptions
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove user from activity");
    }
  }

}
