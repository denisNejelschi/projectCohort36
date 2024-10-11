package gr36.clubActiv.security.security_controller;

import gr36.clubActiv.domain.dto.UserResponseDto;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.security.sec_dto.RefreshRequestDto;
import gr36.clubActiv.security.sec_dto.TokenResponseDto;
import gr36.clubActiv.security.security_service.AuthService;
import gr36.clubActiv.services.interfaces.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  // Endpoint for user login
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    try {
      TokenResponseDto tokenResponse = authService.login(user);
      return ResponseEntity.ok(tokenResponse);
    } catch (AuthException e) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password or username.");
    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
  }

  // Endpoint for refreshing the access token
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDto> getNewAccessToken(
      @RequestBody RefreshRequestDto request) {
    try {
      TokenResponseDto tokenResponse = authService.getNewAccessToken(request.getRefreshToken());
      return ResponseEntity.ok(tokenResponse);
    } catch (AuthException e) {
      throw new RuntimeException("Failed to refresh token", e);
    }
  }

  // Endpoint to get current authenticated user information
  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    String username = authentication.getName();  // Get username from token

    try {
      User currentUser = userService.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found"));
      return ResponseEntity.ok(new UserResponseDto(currentUser));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }
//logout
  @DeleteMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    String refreshToken = token.substring(7);
    authService.logout(refreshToken);
    return ResponseEntity.ok("Logged out successfully");
  }
}
