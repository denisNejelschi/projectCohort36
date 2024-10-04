package gr36.clubActiv.controller;


import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final BCryptPasswordEncoder encoder;

  public UserController(UserService userService, BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  // 1. Get all users (Admin only)
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> user = userService.findById(id);
    if (user.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(user.get(), HttpStatus.OK);
  }

  //update user by ADMIN or user
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    Optional<User> optionalUser = userService.findById(id);
    if (optionalUser.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    User userToUpdate = optionalUser.get();

    // Check if the authenticated user is an admin or the owner of the profile
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && !userToUpdate.getUsername().equals(currentUsername)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);  // Return 403 if user is neither admin nor the profile owner
    }

    // Update the user's details
    userToUpdate.setEmail(updatedUser.getEmail());

    if (updatedUser.getPassword() != null) {
      userToUpdate.setPassword(encoder.encode(updatedUser.getPassword()));
    }

    User updated = userService.save(userToUpdate);
    return new ResponseEntity<>(updated, HttpStatus.OK);
  }


  // 4. Delete user (Admin only)
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    Optional<User> user = userService.findById(id);
    if (user.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    userService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}







