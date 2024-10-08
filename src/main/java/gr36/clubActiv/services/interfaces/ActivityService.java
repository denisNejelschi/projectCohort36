package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.User;
import java.util.List;

public interface ActivityService {

  ActivityDto create(ActivityDto activityDto, User author);

  List<ActivityDto> getAllActivities();

  ActivityDto update(Long id, ActivityDto dto); // delete id

  ActivityDto getActivityById(Long id);

  void deleteActivity(Long id);

  ActivityDto addUserToActivity(Long activity_id, Long user_id);

  List<ActivityDto> getActivitiesByUserId(Long user_id);

  ActivityDto removeUserFromActivity(Long activity_id, Long user_id);

}
