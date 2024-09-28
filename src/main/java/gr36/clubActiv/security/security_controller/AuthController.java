package gr36.clubActiv.security.security_controller;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.security.sec_dto.RefreshRequestDto;
import gr36.clubActiv.security.sec_dto.TokenResponseDto;
import gr36.clubActiv.security.security_service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
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
}