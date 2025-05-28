package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Address;
import ru.delivery.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressCrudService {

  private final AddressRepository addressRepository;

  public Address getById(Long id) {
    return addressRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Адреса с id = %s не существует".formatted(id)));
  }

}
