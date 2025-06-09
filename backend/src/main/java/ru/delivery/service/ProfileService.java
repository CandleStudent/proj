package ru.delivery.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.CurrentProfileDataDto;
import ru.delivery.dto.NewPasswordDto;
import ru.delivery.dto.NewProfileDataDto;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.mapper.ProfileMapper;
import ru.delivery.security.service.JwtService;
import ru.delivery.service.crud.CustomerCrudService;
import ru.delivery.service.crud.UserCrudService;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final CustomerCrudService customerCrudService;
  private final UserCrudService userCrudService;
  private final ProfileMapper profileMapper;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

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

  @Transactional
  public void updatePassword(String userEmail, @Valid NewPasswordDto newPasswordDto) {
    var user = userCrudService.getByEmail(userEmail);
    boolean isOldPasswordCorrect = passwordEncoder.matches(
        newPasswordDto.getOldPassword(), user.getPasswordHash());
    if (!isOldPasswordCorrect) {
      throw new BusinessLogicException("Старый пароль указан некорректно");
    }
    if (!newPasswordDto.getNewPassword().equals(newPasswordDto.getNewPasswordRepeated())) {
      throw new BusinessLogicException("Новый пароль и его повторение не совпадают");
    }

    user.setPasswordHash(passwordEncoder.encode(newPasswordDto.getNewPassword()));
    userCrudService.saveOrUpdate(user);
  }
}
