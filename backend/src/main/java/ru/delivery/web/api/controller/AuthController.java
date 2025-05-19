package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.LoginRequest;
import ru.delivery.dto.RegisterRequest;
import ru.delivery.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
    try {
      userService.register(request.getEmail(), request.getPassword());
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    boolean success = userService.login(email, password);

    if (success) {
      // Можно добавить JWT-токен, но для простоты просто 200
      return ResponseEntity.ok().body("Вход выполнен успешно");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный email или пароль");
    }
  }

}
