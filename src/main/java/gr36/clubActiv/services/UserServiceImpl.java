package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ConfirmationService;
import gr36.clubActiv.services.interfaces.EmailService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

  public UserServiceImpl(
      UserRepository repository, RoleService roleService,
      EmailService emailService, BCryptPasswordEncoder encoder,
      ConfirmationService confirmationService
  ) {
    this.repository = repository;
    this.roleService = roleService;
    this.emailService = emailService;
    this.encoder = encoder;
    this.confirmationService = confirmationService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format("User %s not found", username))
    );
  }

  @Override
  public void register(User user) {
    String username = user.getUsername();
    User existedUser = repository.findByUsername(username).orElse(null);

    if (existedUser != null && existedUser.isActive()) {
      throw new RuntimeException("User already exists");
    }

    if (existedUser == null) {
      existedUser = new User();
      existedUser.setUsername(username);
      existedUser.setRoles(Set.of(roleService.getRoleUser()));
    }

    existedUser.setPassword(encoder.encode(user.getPassword()));
    existedUser.setEmail(user.getEmail());

    repository.save(existedUser);
    emailService.sendConfirmationEmail(existedUser);
  }

  @Override
  @Transactional
  public void registrationConfirm(String code) {
    User user = confirmationService.getUserByConfirmationCode(code);
    user.setActive(true);
  }

//  @Override
//  @Transactional
//  public void registrationConfirm(String code) {
//    User user = confirmationService.getUserByConfirmationCode(code);
//    user.setActive(true);
//  }
}
