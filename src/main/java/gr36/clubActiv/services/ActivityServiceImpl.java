package gr36.clubActiv.services;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.User;
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
  @Transactional
  public ActivityDto create(ActivityDto dto) {
    Activity entity = mappingService.mapDtoToEntity(dto);
    repository.save(entity);
    return mappingService.mapEntityToDto(entity);
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
        .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));
    return mappingService.mapEntityToDto(activity);
  }

  @Override
  @Transactional
  public ActivityDto update(Long id, ActivityDto dto) {
    Activity activity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));

    // Update fields as necessary
    activity.setAddress(dto.getAddress());
    // Update other fields as needed

    Activity updated = repository.save(activity);
    return mappingService.mapEntityToDto(updated);
  }

  @Override
  @Transactional
  public void deleteActivity(Long id) {
    Activity activity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Activity not found with ID: " + id));
    repository.delete(activity);
  }

  @Override
  public ActivityDto addUserToActivity(Long activity_id, Long user_id) {

    Activity activity = repository.findById(activity_id)
        .orElseThrow(() -> new RuntimeException("Activity not found"));
    User user = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Проверяем, добавлен ли пользователь
    if (!activity.getUsers().contains(user)) {
      activity.getUsers().add(user);
      repository.save(activity);  // Сохраняем изменения
    } else {
         return mappingService.mapEntityToDto(activity);
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

  // Uncomment and implement these methods as needed
  // @Override
  // public List<ActivityDto> getActivitiesByUserId(Long userId) {
  //   // Implement logic to fetch activities by user_id
  // }

  // @Override
  // public ActivityDto addUserToActivity(Long activityId, Long userId) {
  //   // Implement logic to add a user to an activity
  // }
}
