package gr36.clubActiv.controller;


import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN') or  @userSecurity.isCurrentUser(#id)")
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    return userService.findById(id)
        .map(userToUpdate -> {
          userToUpdate.setEmail(updatedUser.getEmail());
          if (updatedUser.getPassword() != null) {
            userToUpdate.setPassword(encoder.encode(updatedUser.getPassword()));
          }
          return new ResponseEntity<>(userService.save(userToUpdate), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(user -> {

          if (userService.isLastAdmin(id)) {

            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
          }
          userService.delete(id);
          return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }


}







