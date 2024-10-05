package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.Response;
import gr36.clubActiv.services.interfaces.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

  private final UserService service;

  public RegistrationController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public Response register(@RequestBody User user) {
    service.register(user);
    return new Response("Registration complete. Please check your email.");
  }

  @GetMapping
  public Response registrationConfirm(@RequestParam String code) {
    service.registrationConfirm(code);
    return new Response("Registration confirmed successfully");
  }
}
