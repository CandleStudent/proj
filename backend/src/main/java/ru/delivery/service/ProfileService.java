package ru.delivery.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.CurrentProfileDataDto;
import ru.delivery.dto.NewProfileDataDto;
import ru.delivery.mapper.ProfileMapper;
import ru.delivery.service.crud.CustomerCrudService;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final CustomerCrudService customerCrudService;
  private final ProfileMapper profileMapper;

  @Transactional
  public CurrentProfileDataDto getCurrentProfileData(String userEmail) {
    var customer = customerCrudService.getByEmail(userEmail);
    var currentProfileData = profileMapper.customerAndEmailToCurrentProfileDataDto(customer);

    return currentProfileData;
  }

  @Transactional
  public void updateProfileData(String userEmail, @Valid NewProfileDataDto profileData) {
    var customer = customerCrudService.getByEmail(userEmail);
    customer
        .setName(profileData.getName())
        .setSurname(profileData.getSurname())
        .setPhone(profileData.getPhone());

    customerCrudService.saveOrUpdate(customer);
  }
}
