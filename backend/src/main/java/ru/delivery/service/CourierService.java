package ru.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.CourierStatus;
import ru.delivery.service.crud.CourierCrudService;

@Service
@RequiredArgsConstructor
public class CourierService {

  private final CourierCrudService courierCrudService;

  @Transactional
  public void setReadyForDeliveryStatus(String userEmail) {
    var courier = courierCrudService.getByEmail(userEmail);
    
    courier.setStatus(CourierStatus.READY);
    courierCrudService.saveOrUpdate(courier);
  }

}
