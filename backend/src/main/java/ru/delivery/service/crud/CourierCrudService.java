package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Courier;
import ru.delivery.repository.CourierRepository;

@RequiredArgsConstructor
@Service
public class CourierCrudService {

  private final static String COURIER_WITH_EMAIL_NOT_FOUND_MSG =
      "Courier with email = %s not found";

  private final CourierRepository courierRepository;

  public Courier getByEmail(String email) {
    return courierRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(
            COURIER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public Courier getByEmailWithOrders(String email) {
    return courierRepository.findByEmailWithOrders(email)
        .orElseThrow(() -> new EntityNotFoundException(
            COURIER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public Courier saveOrUpdate(Courier courier) {
    return courierRepository.save(courier);
  }

}
