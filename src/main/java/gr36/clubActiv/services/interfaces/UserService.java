package gr36.clubActiv.services.interfaces;


import gr36.clubActiv.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  //UserDetailsService loadUserByUsername(String username) throws UsernameNotFoundException;
  void register(User user);

  void registrationConfirm(String code);

}
