package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.dto.ActivityDto;
import java.util.List;

public interface ActivityService {

  ActivityDto create(ActivityDto activityDto);

  List<ActivityDto> getAllActivities();

  ActivityDto update(Long id, ActivityDto dto);

  ActivityDto getActivityById(Long id);

  void deleteActivity(Long id);

  ActivityDto addUserToActivity(Long activity_id, Long user_id);

  List<ActivityDto> getActivitiesByUserId(Long user_id);
//  List<ActivityDto> getActivitiesByUserId(Long user_id);
//  ActivityDto addUserToActivity(Long activity_id, Long user_id);

}
