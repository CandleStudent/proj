package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {

  NEW("Приняли в работу"),
  COOKING("Готовим"),
  PACKING("Собираем"),
  IN_DELIVERY("У курьера"),
  DONE("Завершен");

  private final String statusDescription;

  OrderStatus(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  @JsonCreator
  public OrderStatus fromValue(String text) {
    return Enum.valueOf(OrderStatus.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
