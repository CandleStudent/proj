package ru.delivery.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.delivery.dto.RestaurantDto;
import ru.delivery.entity.Restaurant;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RestaurantMapper {

  @Mapping(target = "lat", source = "address.lat")
  @Mapping(target = "lon", source = "address.lon")
  @Mapping(target = "city", source = "address.city")
  @Mapping(target = "street", source = "address.street")
  @Mapping(target = "building", source = "address.building")
  RestaurantDto restaurantToRestaurantDto(Restaurant restaurant);

  List<RestaurantDto> restaurantsToRestaurantDtos(List<Restaurant> restaurants);

}
