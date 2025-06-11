package ru.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.CourierStatus;
import ru.delivery.dto.CourierInfoDto;
import ru.delivery.mapper.CourierMapper;
import ru.delivery.service.crud.CourierCrudService;

@Service
@RequiredArgsConstructor
public class CourierService {

  private final CourierCrudService courierCrudService;
  private final CourierMapper courierMapper;

  @Transactional
  public void setCourierStatus(String userEmail, String status) {
    var courier = courierCrudService.getByEmail(userEmail);
    var statusEnum = CourierStatus.valueOf(status);
    
    courier.setStatus(statusEnum);
    courierCrudService.saveOrUpdate(courier);
  }

  @Transactional(readOnly = true)
  public CourierInfoDto getCourierInfo(String userEmail) {
    var courier = courierCrudService.getByEmail(userEmail);
    return courierMapper.courierToCourierInfoDto(courier);
  }
}
