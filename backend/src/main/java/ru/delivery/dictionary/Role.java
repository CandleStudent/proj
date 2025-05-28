package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

  CUSTOMER, RESTAURANT_MASTER, COURIER;

  @JsonCreator
  public Role fromValue(String text) {
    return Enum.valueOf(Role.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
