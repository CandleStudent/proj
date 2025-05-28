package ru.delivery.dictionary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {

  ACTIVE, BLOCKED, DEACTIVATED;

  @JsonCreator
  public UserStatus fromValue(String text) {
    return Enum.valueOf(UserStatus.class, text);
  }

  @JsonValue
  public String toValue() {
    return this.name();
  }

}
