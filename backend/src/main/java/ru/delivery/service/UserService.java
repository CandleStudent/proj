package ru.delivery.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.Role;
import ru.delivery.dictionary.UserStatus;
import ru.delivery.entity.Customer;
import ru.delivery.entity.User;
import ru.delivery.repository.CustomerRepository;
import ru.delivery.repository.UserRepository;
import ru.delivery.security.service.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  @Transactional
  public String register(String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User()
        .setEmail(email)
        .setPasswordHash(passwordEncoder.encode(password))
        .setRole(Role.CUSTOMER)
        .setStatus(UserStatus.ACTIVE);
    user = userRepository.save(user);

    Customer customer = new Customer().setUser(user);
    customer = customerRepository.save(customer);

    String jwt = jwtService.generateToken(user);

    return jwt;
  }

  public String login(String email, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      throw new RuntimeException("Пользователя с таким e-mail нет");
    }

    User user = userOpt.get();

    boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPasswordHash());
    if (!isPasswordCorrect) {
      throw new RuntimeException("Введен неправильный пароль");
    }

    return jwtService.generateToken(user);
  }

}
