package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.AddressDto;
import ru.delivery.service.AddressService;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public List<AddressDto> getAddresses(Principal principal) {

    var userEmail = principal.getName();
    var addressDto = addressService.getCurrentAddresses(userEmail);

    return addressDto;
  }

  @PostMapping
  public void postAddress(
      Principal principal,
      @Valid @RequestBody AddressDto addressDto) {

    var userEmail = principal.getName();

    addressService.addOrUpdateAddress(userEmail, addressDto);
  }

}
