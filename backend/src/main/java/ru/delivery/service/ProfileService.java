package ru.delivery.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.CurrentProfileDataDto;
import ru.delivery.dto.NewProfileDataDto;
import ru.delivery.mapper.ProfileMapper;
import ru.delivery.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final CustomerRepository customerRepository;
  private final ProfileMapper profileMapper;

  @Transactional
  public CurrentProfileDataDto getCurrentProfileData(String userEmail) {
    var customer = customerRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("customer not found"));
    var currentProfileData = profileMapper.customerAndEmailToCurrentProfileDataDto(customer);

    return currentProfileData;
  }

  @Transactional
  public void updateProfileData(String userEmail, @Valid NewProfileDataDto profileData) {
    var customer = customerRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("customer not found"));
    customer
        .setName(profileData.getName())
        .setSurname(profileData.getSurname())
        .setPhone(profileData.getPhone());

    customerRepository.save(customer);
  }
}
