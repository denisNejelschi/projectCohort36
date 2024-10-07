package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserAlreadyExistsException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ConfirmationCodeRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserRepository repository;
  private final RoleService roleService;
  private final EmailService emailService;
  private final BCryptPasswordEncoder encoder;
  private final ConfirmationService confirmationService;
  private final ConfirmationCodeRepository confirmationCodeRepository;
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository repository, RoleService roleService,
      EmailService emailService, BCryptPasswordEncoder encoder,
      ConfirmationService confirmationService,
      ConfirmationCodeRepository confirmationCodeRepository, UserRepository userRepository) {
    this.repository = repository;
    this.roleService = roleService;
    this.emailService = emailService;
    this.encoder = encoder;
    this.confirmationService = confirmationService;
    this.confirmationCodeRepository = confirmationCodeRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException(String.format("User %s not found", username)));
  }

  @Override
  public void register(User user) {
    Optional<User> existingUser = repository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExistsException("Email already registered");
    }

    User newUser = new User();
    newUser.setUsername(user.getUsername());
    newUser.setRoles(Set.of(roleService.getRoleUser()));
    newUser.setEmail(user.getEmail());
    newUser.setPassword(encoder.encode(user.getPassword()));
    newUser.setImage(user.getImage());
    newUser.setActive(false);

    repository.save(newUser);
    emailService.sendConfirmationEmail(newUser);
  }

  @Override
  public void registrationConfirm(String code) {
    User user = confirmationService.getUserByConfirmationCode(code);
    user.setActive(true);
  }

  @Override
  public List<User> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<User> findById(Long id) {
    return repository.findById(id).or(() -> Optional.empty());
  }

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    confirmationCodeRepository.deleteByUserId(id);
    repository.deleteById(id);
  }

  @Override
  public boolean isLastAdmin(Long userId) {
    User user = repository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    boolean isAdmin = user.getRoles().contains(roleService.getRoleAdmin());

    if (isAdmin) {
      long adminCount = repository.countByRole("ROLE_ADMIN");
      return adminCount <= 1;  // Prevent deletion if last admin

    }

    return false;
  }

  @Override
  public boolean existsByUsername(String username) {

    Optional<User> user = userRepository.findByUsername(username);

    return user.isPresent();
  }

  @Override
  public int countAdmins() {
    Role adminRole = roleService.getRoleAdmin();
    return userRepository.countByRolesContaining(adminRole);
  }



}



