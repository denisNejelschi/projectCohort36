package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final RoleService roleService;
  private final EmailService emailService;
  private final BCryptPasswordEncoder encoder;
  private final ConfirmationService confirmationService;
  private final UserRepository userRepository;
  private final ConfirmationCodeRepository confirmationCodeRepository;  // Inject ConfirmationCodeRepository

  public UserServiceImpl(
      UserRepository repository, RoleService roleService,
      EmailService emailService, BCryptPasswordEncoder encoder,
      ConfirmationService confirmationService,
      UserRepository userRepository, ConfirmationCodeRepository confirmationCodeRepository) {
    this.repository = repository;
    this.roleService = roleService;
    this.emailService = emailService;
    this.encoder = encoder;
    this.confirmationService = confirmationService;
    this.userRepository = userRepository;
    this.confirmationCodeRepository = confirmationCodeRepository;  // Assign ConfirmationCodeRepository
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format("User %s not found", username))
    );
  }

  @Override
  public void register(User user) {
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new IllegalArgumentException("Email already registered.");
    }

    User newUser = new User();
    newUser.setUsername(user.getUsername());
    newUser.setRoles(Set.of(roleService.getRoleUser()));
    newUser.setEmail(user.getEmail());
    newUser.setPassword(encoder.encode(user.getPassword()));
    newUser.setActive(false);

    repository.save(newUser);
    emailService.sendConfirmationEmail(newUser);
  }

  @Override
  @Transactional
  public void registrationConfirm(String code) {
    User user = confirmationService.getUserByConfirmationCode(code);
    user.setActive(true);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> findById(Long id) {//TODO only admin or user can see this endpoint

    return Optional.ofNullable(userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id)));

  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    confirmationCodeRepository.deleteByUserId(id);
    repository.deleteById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userRepository.findByEmail(email));
  }
}

//  @Override
//  @Transactional
//  public void registrationConfirm(String code) {
//    User user = confirmationService.getUserByConfirmationCode(code);
//    user.setActive(true);
//  }

