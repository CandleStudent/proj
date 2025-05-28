package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.User;
import ru.delivery.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserCrudService {

  private final UserRepository userRepository;

  public User getByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("юзера с email = %s не существует"
            .formatted(email)));
  }

  public boolean isUserExists(String email) {
    return userRepository.existsByEmail(email);
  }

  public User saveOrUpdate(User user) {
    return userRepository.save(user);
  }

}
