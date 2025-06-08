package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CourierStatus {

  READY, IN_DELIVERY, BUSY, INACTIVE;

  @JsonCreator
  public CourierStatus fromValue(String text) {
    return Enum.valueOf(CourierStatus.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
