package ru.delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.AddressDto;
import ru.delivery.entity.Address;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.service.crud.CustomerCrudService;
import ru.delivery.web.client.dto.YandexSuggestResponse;
import ru.delivery.web.client.service.YandexSuggestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

  private final CustomerCrudService customerCrudService;
  private final AddressMapper addressMapper;
  private final YandexSuggestService yandexSuggestService;

  @Transactional
  public void addOrUpdateAddress(String userEmail, @Valid AddressDto addressDto) {
    // todo временно
  //  addressDto = validateAddressWithGeoSuggester(addressDto);

    var customer = customerCrudService.getByEmailWithAddresses(userEmail);
    if (addressDto.getId() == null) {
      customer.addAddress(addressMapper.addressDtoToAddress(addressDto, customer));
    } else {
      final var addressId = addressDto.getId();
      var changingAddress = customer.getAddresses().stream()
          .filter(address -> address.getId().equals(addressId)).findAny().orElseThrow(
              () -> new BusinessLogicException("Среди адресов клиента нет с указанным айди = %s"
                  .formatted(addressId)));
      copyFromAddressDtoToAddress(addressDto, changingAddress);
    }
    customerCrudService.saveOrUpdate(customer);
  }

  private void copyFromAddressDtoToAddress(AddressDto addressDto, Address address) {
    address.setCity(addressDto.getCity());
    address.setStreet(addressDto.getStreet());
    address.setBuilding(addressDto.getBuilding());
    address.setEntrance(addressDto.getEntrance());
    address.setFloor(addressDto.getFloor());
    address.setApartments(addressDto.getApartments());
    address.setComment(addressDto.getComment());
    address.setLat(addressDto.getLat());
    address.setLon(addressDto.getLon());
  }

  /**
   * Получаемый адрес отправляется в Yandex GeoSuggest, где ищутся соответствия ему. Если таковое
   * находится, то его мы и заносим в БД
   *
   * @param addressDto
   */
  private AddressDto validateAddressWithGeoSuggester(@Valid AddressDto addressDto) {
    var suggesterResponse = yandexSuggestService.suggest(
            addressDto.getCity() + " " + addressDto.getStreet() + " " + addressDto.getBuilding())
        .block();
    if (suggesterResponse.getResults().isEmpty()) {
      throw new BusinessLogicException("Некорректный адрес. GeoSuggest не смог найти соответствия");
    }
    var addressFromSuggester = suggesterResponse.getResults().get(0).getAddress();
    log.info(addressFromSuggester.getFormatted_address());

    var componentMap = YandexSuggestResponse.createComponentMap(addressFromSuggester);
    return addressDto
        .setCity(componentMap.get("LOCALITY"))
        .setStreet(componentMap.get("STREET"))
        .setBuilding(componentMap.get("HOUSE"));
  }

  @Transactional(readOnly = true)
  public List<AddressDto> getCurrentAddresses(String userEmail) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    return addressMapper.addressesToAddressDtos(customer.getAddresses());
  }

  @Transactional
  public void deleteAddress(String userEmail, Long id) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);
    var deletingAddress = customer.getAddresses().stream()
        .filter(address -> address.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException("Вы пытаетесь удалить не свой адрес"));
    customer.getAddresses().remove(deletingAddress);

    customerCrudService.saveOrUpdate(customer);
  }
}
