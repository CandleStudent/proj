package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.auth.AuthenticationRequest;
import ru.delivery.dto.auth.AuthenticationResponse;
import ru.delivery.dto.auth.RegisterRequest;
import ru.delivery.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
    try {
      String jwt = userService.register(request.getEmail(), request.getPassword());
      return ResponseEntity.ok(new AuthenticationResponse(jwt));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    var jwt = userService.login(email, password);;

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

}
