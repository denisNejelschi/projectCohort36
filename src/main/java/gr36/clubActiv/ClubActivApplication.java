package gr36.clubActiv;

import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.interfaces.RoleService;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ClubActivApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClubActivApplication.class, args);
  }
}


