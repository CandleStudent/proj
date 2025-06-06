package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

  ROLE_CUSTOMER, ROLE_RESTAURANT_ADMIN, ROLE_COURIER;

  @JsonCreator
  public Role fromValue(String text) {
    return Enum.valueOf(Role.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
