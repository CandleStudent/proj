package ru.delivery.service.crud;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.delivery.entity.Restaurant;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantCrudService {

  private final RestaurantRepository restaurantRepository;

  public Restaurant getById(Long id) {
    return restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            "Ресторана с id = %s не существует".formatted(id)));
  }

  public List<Restaurant> getAllWithAddresses() {
    var restaurants =  restaurantRepository.findAllWithAddress();
    if (restaurants.isEmpty()) {
      throw new BusinessLogicException("Нет ни одного ресторана в БД");
    }

    return restaurants;
  }

}
