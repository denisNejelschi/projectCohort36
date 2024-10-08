package gr36.clubActiv.security.security_controller;

import gr36.clubActiv.domain.dto.UserResponseDto;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.security.sec_dto.RefreshRequestDto;
import gr36.clubActiv.security.sec_dto.TokenResponseDto;
import gr36.clubActiv.security.security_service.AuthService;
import gr36.clubActiv.services.interfaces.UserService;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

  private AuthService service;
  private UserService userService;

  public AuthController(AuthService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  //endpoint для аутентификации пользователя
  @PostMapping("/login")
  public TokenResponseDto login(@RequestBody User user) throws AuthException {
    try {
      return service.login(user);
    } catch (AuthException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("/refresh")
  public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto request) {
    try {
      return service.getNewAccessToken(request.getRefreshToken());
    } catch (AuthException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/user")
  public ResponseEntity<?> getCurrentUser(Authentication authentication) {

    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      UserDetails userDetails = userService.loadUserByUsername(username);
      // Преобразуйте UserDetails в DTO, если нужно
      return ResponseEntity.ok(userDetails);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
  }

}