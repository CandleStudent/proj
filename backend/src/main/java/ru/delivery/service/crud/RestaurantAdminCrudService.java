package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.RestaurantAdmin;
import ru.delivery.repository.RestaurantAdminRepository;

@RequiredArgsConstructor
@Service
public class RestaurantAdminCrudService {

  private final static String USER_WITH_EMAIL_NOT_FOUND_MSG =
      "RestaurantAdmin with email = %s not found";
  private final static Set<OrderStatus> ACTIVE_STATUSES =
      Set.of(OrderStatus.NEW, OrderStatus.COOKING, OrderStatus.PACKING, OrderStatus.IN_DELIVERY);

  private final RestaurantAdminRepository restaurantAdminRepository;

  public RestaurantAdmin getByEmail(String email) {
    return restaurantAdminRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(
            USER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin getByEmailWithOrders(String email) {
    return restaurantAdminRepository.findByEmailWithOrders(email)
        .orElseThrow(() -> new EntityNotFoundException(
            USER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin getByEmailWithActiveOrders(String email) {
    return restaurantAdminRepository.findByEmailWithActiveOrders(email, ACTIVE_STATUSES)
        .orElseThrow(() -> new EntityNotFoundException(
            USER_WITH_EMAIL_NOT_FOUND_MSG.formatted(email)));
  }

  public RestaurantAdmin saveOrUpdate(RestaurantAdmin restaurantAdmin) {
    return restaurantAdminRepository.save(restaurantAdmin);
  }

}
