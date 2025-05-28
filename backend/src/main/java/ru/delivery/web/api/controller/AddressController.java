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
import ru.delivery.dto.AddressDto;
import ru.delivery.service.AddressService;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public ResponseEntity<?> getAddresses(Principal principal) {

    var userEmail = principal.getName();
    var addressDto = addressService.getCurrentAddresses(userEmail);

    try {
      return ResponseEntity.ok(addressDto);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> postAddress(
      Principal principal,
      @Valid @RequestBody AddressDto addressDto) {

    var userEmail = principal.getName();

    try {
      addressService.addAddress(userEmail, addressDto);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
