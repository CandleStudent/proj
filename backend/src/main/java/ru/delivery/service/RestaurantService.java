package ru.delivery.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delivery.dto.RestaurantDto;
import ru.delivery.mapper.RestaurantMapper;
import ru.delivery.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final RestaurantMapper restaurantMapper;

  public List<RestaurantDto> getAllRestaurants() {
    var restaurants = restaurantRepository.findAllWithAddress();

    return restaurantMapper.restaurantsToRestaurantDtos(restaurants);
  }

}
