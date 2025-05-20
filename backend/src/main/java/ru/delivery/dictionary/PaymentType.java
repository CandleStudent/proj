package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentType {

  CARD, CASH;

  @JsonCreator
  public PaymentType fromValue(String text) {
    return Enum.valueOf(PaymentType.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
