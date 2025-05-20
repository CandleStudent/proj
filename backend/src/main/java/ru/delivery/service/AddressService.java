package ru.delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.AddressDto;
import ru.delivery.entity.CustomerAddress;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.repository.AddressRepository;
import ru.delivery.repository.CustomerRepository;

@Service
@RequiredArgsConstructor

public class AddressService {

  private final CustomerRepository customerRepository;
  private final AddressMapper addressMapper;
  private final AddressRepository addressRepository;

  @Transactional
  public void addAddress(String userEmail, @Valid AddressDto addressDto) {
    var customer = customerRepository.findByEmailWithAddresses(userEmail)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
    var address = addressRepository.save(addressMapper.addressDtoToAddress(addressDto));
    customer.addAddress(
        new CustomerAddress()
            .setAddress(address)
            .setCustomer(customer)
            .setComment(addressDto.getComment()));
    customerRepository.save(customer);
  }

  @Transactional(readOnly = true)
  public List<AddressDto> getCurrentAddresses(String userEmail) {
    var customer = customerRepository.findByEmailWithAddresses(userEmail)
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    return addressMapper.addressesToAddressDtos(customer.getAddresses()
        .stream()
        .map(CustomerAddress::getAddress)
        .toList());
  }
}
