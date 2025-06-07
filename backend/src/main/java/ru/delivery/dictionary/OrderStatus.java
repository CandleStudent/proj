package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderStatus {

  NEW("Приняли в работу" ,1),
  COOKING("Готовим", 2),
  PACKING("Собираем", 3),
  IN_DELIVERY("У курьера", 4),
  DONE("Завершен", 5);

  private final String statusDescription;
  private final Integer orderNumber;

  OrderStatus(String statusDescription, Integer orderNumber) {
    this.statusDescription = statusDescription;
    this.orderNumber = orderNumber;
  }

  public static OrderStatus fromOrderNumber(Integer orderNumber) {
    for (OrderStatus status : values()) {
      if (status.getOrderNumber().equals(orderNumber)) {
        return status;
      }
    }
    throw new IllegalArgumentException("No matching OrderStatus with number: " + orderNumber);
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
