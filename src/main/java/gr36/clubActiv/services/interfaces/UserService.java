package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.dto.UserDto;
import java.util.List;
import java.util.Optional;

public interface UserService {

  List<UserDto> getAllUsers();

  Optional<UserDto> getUserById(Long id);

  UserDto createUser(UserDto userDto);

  UserDto updateUser(Long id, UserDto userDto);

  void deleteUser(Long id);

}