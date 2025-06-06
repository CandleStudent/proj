package ru.delivery.service;

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
import ru.delivery.security.service.JwtService;
import ru.delivery.service.crud.UserCrudService;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserCrudService userCrudService;
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  @Transactional
  public String register(String email, String password) {
    if (userCrudService.isUserExists(email)) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User()
        .setEmail(email)
        .setPasswordHash(passwordEncoder.encode(password))
        .setRole(Role.ROLE_CUSTOMER)
        .setStatus(UserStatus.ACTIVE);
    user = userCrudService.saveOrUpdate(user);

    Customer customer = new Customer().setUser(user);
    customer = customerRepository.save(customer);

    String jwt = jwtService.generateToken(user);

    return jwt;
  }

  public String login(String email, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    var user = userCrudService.getByEmail(email);

    boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPasswordHash());
    if (!isPasswordCorrect) {
      throw new RuntimeException("Введен неправильный пароль");
    }

    return jwtService.generateToken(user);
  }

}
