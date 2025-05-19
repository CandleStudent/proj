package ru.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.delivery.entity.User;
import ru.delivery.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User();
    user.setEmail(email);
    user.setPasswordHash(passwordEncoder.encode(password));
    userRepository.save(user);
  }

}
