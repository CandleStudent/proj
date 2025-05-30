package ru.delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dto.AddressDto;
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
  public void addAddress(String userEmail, @Valid AddressDto addressDto) {
    addressDto = validateAddressWithGeoSuggester(addressDto);

    var customer = customerCrudService.getByEmailWithAddresses(userEmail);
    customer.addAddress(addressMapper.addressDtoToAddress(addressDto, customer));
    customerCrudService.saveOrUpdate(customer);
  }

  /**
   * Получаемый адрес отправляется в Yandex GeoSuggest, где ищутся соответствия ему.
   * Если таковое находится, то его мы и заносим в БД
   * @param addressDto
   */
  private AddressDto validateAddressWithGeoSuggester(@Valid AddressDto addressDto) {
    var suggesterResponse = yandexSuggestService.suggest(addressDto.getCity() + " " + addressDto.getStreet() + " " + addressDto.getBuilding()).block();
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

}
