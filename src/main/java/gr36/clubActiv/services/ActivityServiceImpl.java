package gr36.clubActiv.services;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.repository.ActivityRepository;
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

  public ActivityServiceImpl(ActivityRepository repository, ActivityMappingService mappingService) {
    this.repository = repository;
    this.mappingService = mappingService;
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
