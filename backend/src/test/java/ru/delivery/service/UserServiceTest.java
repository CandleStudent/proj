package ru.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.delivery.PostgresContainerTest;
import ru.delivery.dictionary.Role;
import ru.delivery.dictionary.UserStatus;
import ru.delivery.dto.auth.RegisterRequest;
import ru.delivery.entity.User;
import ru.delivery.repository.CustomerRepository;
import ru.delivery.repository.UserRepository;
import ru.delivery.security.service.JwtService;


public class UserServiceTest extends PostgresContainerTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @MockitoBean
  private PasswordEncoder passwordEncoder;

  @MockitoBean
  private JwtService jwtService;

  @MockitoBean
  private AuthenticationManager authenticationManager;

  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Test
  public void registerTest() {
    var registerRequest = Instancio.create(RegisterRequest.class);

    doReturn("jwt")
        .when(jwtService).generateToken(any(User.class));
    doReturn("hashedPassword")
        .when(passwordEncoder).encode(any());

    var jwt = userService.register(registerRequest.getEmail(), registerRequest.getPassword());

    assertEquals("jwt", jwt);

    var savedUser = userRepository.findByEmail(registerRequest.getEmail()).get();
    assertEquals("hashedPassword", savedUser.getPassword());
    assertEquals(Role.ROLE_CUSTOMER, savedUser.getRole());
    assertEquals(UserStatus.ACTIVE, savedUser.getStatus());
    var savedCustomer = customerRepository.findByEmail(registerRequest.getEmail()).get();
  }

}
