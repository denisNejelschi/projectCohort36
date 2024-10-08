package gr36.clubActiv.services;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.ActivityCreationException;
import gr36.clubActiv.exeption_handling.exeptions.ActivityNotFoundException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ActivityRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.mapping.ActivityMappingService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository repository;
  private final ActivityMappingService mappingService;
  private final UserRepository userRepository;

  public ActivityServiceImpl(ActivityRepository repository, ActivityMappingService mappingService,
      UserRepository userRepository) {
    this.repository = repository;
    this.mappingService = mappingService;
    this.userRepository = userRepository;
  }

  @Override
  public ActivityDto create(ActivityDto activityDto, User author) {
    Activity activity = new Activity();
    activity.setTitle(activityDto.getTitle());
    activity.setDescription(activityDto.getDescription());
    activity.setStartDate(activityDto.getStartDate());
    activity.setAddress(activityDto.getAddress());
    activity.setAuthor(author); // Assign the logged-in user as the author

    // Use the repository to save the activity
    repository.save(activity);
    return new ActivityDto(activity);
  }


  @Override
  public List<ActivityDto> getAllActivities() {
    return repository.findAll()
        .stream()
        .map(mappingService::mapEntityToDto)
        .toList();
  }

  @Override
  public ActivityDto getActivityById(Long id) {
    Activity activity = repository.findById(id)
        .orElseThrow(() -> new ActivityNotFoundException(id));
    return mappingService.mapEntityToDto(activity);
  }

  @Override
  @Transactional
  public ActivityDto update(Long id, ActivityDto dto) {
    Activity activity = repository.findById(id)
        .orElseThrow(() -> new ActivityNotFoundException(id));
    activity.setAddress(dto.getAddress());
    return mappingService.mapEntityToDto(activity);
  }

  @Override
  @Transactional
  public void deleteActivity(Long id) {
    Activity activity = repository.findById(id)
        .orElseThrow(() -> new ActivityNotFoundException(id));
    repository.delete(activity);
  }

  @Override
  @Transactional
  public ActivityDto addUserToActivity(Long activity_id, Long user_id) {

    Activity activity = repository.findById(activity_id)
        .orElseThrow(() -> new ActivityNotFoundException(activity_id));
    User user = userRepository.findById(user_id)
        .orElseThrow(() -> new UserNotFoundException(user_id));

    if (!activity.getUsers().contains(user)) {
      activity.addUser(user);
    }

    return mappingService.mapEntityToDto(activity);
  }

  @Override
  public List<ActivityDto> getActivitiesByUserId(Long user_id) {
    List<Activity> activities = repository.findByUsersId(user_id);
    return activities.stream()
        .map(mappingService::mapEntityToDto)
        .toList();
  }

  @Transactional
  public ActivityDto removeUserFromActivity(Long activity_id, Long user_id) {
    Activity activity = repository.findById(activity_id)
        .orElseThrow(() -> new ActivityNotFoundException(activity_id));
    User user = userRepository.findById(user_id)
        .orElseThrow(() -> new UserNotFoundException(user_id));

    if (activity.getUsers().contains(user)) {
      activity.getUsers().remove(user);  // Remove the user from the activity's user collection
      repository.save(activity);  // Save the updated activity
    } else {
      throw new RuntimeException("User is not part of this activity.");
    }

    return mappingService.mapEntityToDto(activity);  // Return the updated activity as a DTO
  }

}
