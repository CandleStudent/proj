package ru.delivery.service.crud;

import static ru.delivery.constants.YummyConstant.OrderConstant.ACTIVE_STATUSES;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.entity.RestaurantAdmin;
import ru.delivery.repository.RestaurantAdminRepository;

@RequiredArgsConstructor
@Service
public class RestaurantAdminCrudService {

  private final static String RESTAURANT_ADMIN_WITH_EMAIL_NOT_FOUND_MSG =
      "RestaurantAdmin with email = %s not found";

  private final RestaurantAdminRepository restaurantAdminRepository;

  public RestaurantAdmin getByEmail(String email) {
    return restaurantAdminRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(
            RESTAURANT_ADMIN_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin getByEmailWithOrders(String email) {
    return restaurantAdminRepository.findByEmailWithOrders(email)
        .orElseThrow(() -> new EntityNotFoundException(
            RESTAURANT_ADMIN_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin getByEmailWithActiveOrders(String email) {
    return restaurantAdminRepository.findByEmailWithActiveOrders(email, ACTIVE_STATUSES)
        .orElseThrow(() -> new EntityNotFoundException(
            RESTAURANT_ADMIN_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin saveOrUpdate(RestaurantAdmin restaurantAdmin) {
    return restaurantAdminRepository.save(restaurantAdmin);
  }

}
