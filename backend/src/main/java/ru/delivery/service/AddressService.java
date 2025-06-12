package ru.delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.AddressDto;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.service.crud.AddressCrudService;
import ru.delivery.service.crud.CustomerCrudService;
import ru.delivery.web.client.service.YandexGeocoderService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

  private final CustomerCrudService customerCrudService;
  private final AddressCrudService addressCrudService;
  private final AddressMapper addressMapper;
  private final YandexGeocoderService yandexGeocoderService;

  @Transactional
  public void addAddress(String userEmail, @Valid AddressDto addressDto) {
    //todo split this method on more specific and SOLID
    var posOfAddress = yandexGeocoderService
        .getLonLatByAddress(addressDto.getFormattedAddress())
        .block();
    var point = Optional.ofNullable(posOfAddress.getPosition())
        .orElseThrow(
            () -> new BusinessLogicException("По данному адресу нет географических точек"));
    addressDto.setLon(point.getLon());
    addressDto.setLat(point.getLat());
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    customer.addAddress(addressMapper.addressDtoToAddress(addressDto, customer));
    customerCrudService.saveOrUpdate(customer);
  }

  @Transactional(readOnly = true)
  public List<AddressDto> getCurrentAddresses(String userEmail) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    return addressMapper.addressesToAddressDtos(customer.getAddresses());
  }

  @Transactional
  public void detachAddressFromCustomer(String userEmail, Long id) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);
    var deletingAddress = customer.getAddresses().stream()
        .filter(address -> address.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException("Вы пытаетесь удалить не свой адрес"));
    deletingAddress.setCustomer(null);

    // мы не удаляем адрес из БД, т.к. на него завязаны заказы. Мы только убираем у него привязку
    // к данному клиенту
    customer.removeAddress(deletingAddress);
    customerCrudService.saveOrUpdate(customer);
    addressCrudService.saveOrUpdate(deletingAddress);
  }
}
