package ru.delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.AddressDto;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.service.crud.CustomerCrudService;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final CustomerCrudService customerCrudService;
  private final AddressMapper addressMapper;

  @Transactional
  public void addAddress(String userEmail, @Valid AddressDto addressDto) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);
    customer.addAddress(addressMapper.addressDtoToAddress(addressDto, customer));
    customerCrudService.saveOrUpdate(customer);
  }

  @Transactional(readOnly = true)
  public List<AddressDto> getCurrentAddresses(String userEmail) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    return addressMapper.addressesToAddressDtos(customer.getAddresses());
  }

}
