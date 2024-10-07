package gr36.clubActiv.controller;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.services.interfaces.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  public ResponseEntity<ActivityDto> create(@RequestBody ActivityDto activity) {
    log.info("Creating new activity: {}", activity);
    ActivityDto createdActivity = service.create(activity);
    log.info("Activity created successfully with ID: {}", createdActivity.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
  }


  @GetMapping
  public List<ActivityDto> getAllActivities() {
    log.info("Fetching all activities");
    return service.getAllActivities();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id) {
      log.info("Fetching activity by ID: {}", id);
      ActivityDto activity = service.getActivityById(id);
      return ResponseEntity.ok(activity);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(
      @PathVariable("id") Long id,
      @RequestBody ActivityDto dto) {
      log.info("Updating activity with ID: {}", id);
      ActivityDto updatedActivity = service.update(id, dto);
      return ResponseEntity.ok(updatedActivity);
  }

//  @DeleteMapping("/{id}")
//  public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
//      log.info("Deleting activity with ID: {}", id);
//      service.deleteActivity(id);
//      return ResponseEntity.noContent().build();
//   }

  @DeleteMapping("/{id}")// TODO creator and admin can delete only
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
    log.info("Attempting to delete activity with ID: {}", id);

    // Проверяем, существует ли активность
    ActivityDto activity = service.getActivityById(id);
    if (activity == null) {
      log.error("Activity with ID {} not found", id);
      return new ResponseEntity<>("Activity not found", HttpStatus.NOT_FOUND);
    }

    // Если активность существует, удаляем ее
    service.deleteActivity(id);
    log.info("Activity with ID {} deleted successfully", id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{activity_id}/add-user/{user_id}") //TODO POST -> PUT
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
    log.info("Delete activities for user ID: {}", user_id); // Логирование
      ActivityDto updatedActivity = service.removeUserFromActivity(activity_id, user_id);
      return ResponseEntity.ok(updatedActivity);
  }

}
