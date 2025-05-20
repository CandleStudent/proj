package ru.delivery.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.delivery.dto.AddressDto;
import ru.delivery.entity.Address;

@Mapper(componentModel = ComponentModel.SPRING)
public interface AddressMapper {

//  @Mapping(target = "rowInsertTime", ignore = true)
  @Mapping(target = "rowUpdateTime", ignore = true)
  @Mapping(target = "id", ignore = true)
  Address addressDtoToAddress(AddressDto addressDto);

  AddressDto addressToAddressDto(Address address);

  List<AddressDto> addressesToAddressDtos(List<Address> address);

}
