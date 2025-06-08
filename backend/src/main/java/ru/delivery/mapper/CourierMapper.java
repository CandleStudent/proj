package ru.delivery.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.delivery.dto.CourierInfoDto;
import ru.delivery.entity.Courier;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CourierMapper {

  CourierInfoDto customerAndEmailToCurrentProfileDataDto(Courier courier);
}
