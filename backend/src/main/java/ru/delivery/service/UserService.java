package ru.delivery.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.entity.Customer;
import ru.delivery.entity.User;
import ru.delivery.repository.CustomerRepository;
import ru.delivery.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void register(String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User()
        .setEmail(email)
        .setPasswordHash(passwordEncoder.encode(password));
    user = userRepository.save(user);

    Customer customer = new Customer().setUser(user);
    customer = customerRepository.save(customer);
  }

  public boolean login(String email, String password) {
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      return false;
    }
    User user = userOpt.get();
    // Сравниваем введённый пароль с хэшем
    return passwordEncoder.matches(password, user.getPasswordHash());
  }

}
