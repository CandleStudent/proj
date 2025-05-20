package ru.delivery.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewProfileDataDto {

  private String name;
  private String surname;

  private String phone;

}
