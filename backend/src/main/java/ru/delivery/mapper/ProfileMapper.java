package ru.delivery.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.delivery.dto.CurrentProfileDataDto;
import ru.delivery.entity.Customer;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProfileMapper {

  @Mapping(target = "email", source = "customer.user.email")
  CurrentProfileDataDto customerAndEmailToCurrentProfileDataDto(Customer customer);
}
