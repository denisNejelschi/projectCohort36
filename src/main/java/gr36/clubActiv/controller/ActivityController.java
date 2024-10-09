package gr36.clubActiv.controller;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityService service;
  private final UserService userService;
  private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

  public ActivityController(ActivityService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @GetMapping("/test")
  public String test() {
    log.info("Test endpoint called");
    return "Hello World";
  }

  @PostMapping
  public ResponseEntity<ActivityDto> create(@RequestBody ActivityDto activityDto, Authentication authentication) {
    log.info("Creating new activity: {}", activityDto);


    String username = authentication.getName();
    User author = userService.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));


    ActivityDto createdActivity = service.create(activityDto, author);
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
  public ResponseEntity<ActivityDto> addUserToActivity(@PathVariable Long activity_id,
      @PathVariable Long user_id) {
    ActivityDto updatedActivity = service.addUserToActivity(activity_id, user_id);
    return ResponseEntity.ok(updatedActivity); // Возвращаем обновленную активность
  }

  @GetMapping("/my-activities")
  public ResponseEntity<List<ActivityDto>> getMyActivities(Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<ActivityDto> activities;
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      activities = service.getAllActivities();
    } else {
      activities = service.getActivitiesByUserId(user.getId());
    }

    return ResponseEntity.ok(activities);
  }


  @DeleteMapping("/{activity_id}/remove-user/{user_id}")
  public ResponseEntity<?> removeUserFromActivity(@PathVariable Long activity_id,
      @PathVariable Long user_id) {
    log.info("Delete activities for user ID: {}", user_id); // Логирование
    ActivityDto updatedActivity = service.removeUserFromActivity(activity_id, user_id);
    return ResponseEntity.ok(updatedActivity);
  }


}
