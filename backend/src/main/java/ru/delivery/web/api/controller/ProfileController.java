package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.CurrentProfileDataDto;
import ru.delivery.dto.NewProfileDataDto;
import ru.delivery.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping
  public CurrentProfileDataDto getProfileData(Principal principal) {

    var userEmail = principal.getName();
    CurrentProfileDataDto currentProfileDataDto = profileService.getCurrentProfileData(userEmail);
    return currentProfileDataDto;
  }

  @PostMapping()
  public ResponseEntity<?> postProfileData(
      Principal principal,
      @Valid @RequestBody NewProfileDataDto profileData) {

    var userEmail = principal.getName();

    try {
      profileService.updateProfileData(userEmail, profileData);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
