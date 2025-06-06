package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CourierStatus {

  FREE, BUSY, ON_WAY, INACTIVE;

  @JsonCreator
  public OrderStatus fromValue(String text) {
    return Enum.valueOf(OrderStatus.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
