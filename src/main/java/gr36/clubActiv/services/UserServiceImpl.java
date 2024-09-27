package gr36.clubActiv.services;

import gr36.clubActiv.domain.dto.UserDto;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.UserService;
import gr36.clubActiv.services.mapping.UserMappingService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMappingService userMappingService;

  public UserServiceImpl(UserRepository userRepository, UserMappingService userMappingService) {
    this.userRepository = userRepository;
    this.userMappingService = userMappingService;
  }

  @Override
  public List<UserDto> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(userMappingService::mapEntityToDto)
        .toList();
  }

  @Override
  public Optional<UserDto> getUserById(Long id) {
    return userRepository.findById(id).map(userMappingService::mapEntityToDto);
  }

  @Override
  @Transactional
  public UserDto createUser(UserDto userDto) {
    User user = userMappingService.mapDtoToEntity(userDto);
    //user.setPassword(passwordEncoder.encode(userDto.getPassword())); TODO passwordEncoder
    user = userRepository.save(user);
    return userMappingService.mapEntityToDto(user);
  }

  @Override
  public UserDto updateUser(Long id, UserDto userDto) {
    return null;
  }

  @Override
  public void deleteUser(Long id) {

  }
}
