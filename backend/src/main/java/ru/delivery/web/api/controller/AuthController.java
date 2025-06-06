package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.auth.AuthenticationRequest;
import ru.delivery.dto.auth.AuthenticationResponse;
import ru.delivery.dto.auth.RegisterRequest;
import ru.delivery.dto.auth.WorkerRegisterRequest;
import ru.delivery.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public AuthenticationResponse register(@RequestBody @Valid RegisterRequest request) {
    String jwt = userService.register(request.getEmail(), request.getPassword());
    return new AuthenticationResponse(jwt);
  }

  @PostMapping("/worker/register")
  @PreAuthorize("hasRole('MASTER')")
  public AuthenticationResponse registerRestaurantAdmin(
      @RequestBody @Valid WorkerRegisterRequest request) {

    String jwt = userService.registerWorker(request);
    return new AuthenticationResponse(jwt);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();

    var jwt = userService.login(email, password);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

}
