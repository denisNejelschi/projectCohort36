package gr36.clubActiv.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import gr36.clubActiv.services.interfaces.UserService;

@Component
public class UserSecurity {

  private final UserService userService;

  public UserSecurity(UserService userService) {
    this.userService = userService;
  }

  public boolean isCurrentUser(Long userId) {
    // Get the current authentication object
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    // Fetch the user from the database using the username
    return userService.findById(userId)
        .map(user -> user.getUsername().equals(currentUsername))
        .orElse(false);
  }
}
