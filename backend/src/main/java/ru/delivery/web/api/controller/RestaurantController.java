package ru.delivery.web.api.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.RestaurantDto;
import ru.delivery.service.RestaurantService;

//todo необходимость контроллера под вопросом
@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;

  @GetMapping
  public List<RestaurantDto> getRestaurants(Principal principal) {
    return restaurantService.getAllRestaurants();
  }

}
