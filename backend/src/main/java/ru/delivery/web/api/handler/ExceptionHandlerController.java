package ru.delivery.web.api.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.web.api.controller.AddressController;
import ru.delivery.web.api.controller.AdminOrderController;
import ru.delivery.web.api.controller.AuthController;
import ru.delivery.web.api.controller.CourierController;
import ru.delivery.web.api.controller.OrderController;
import ru.delivery.web.api.controller.ProfileController;
import ru.delivery.web.api.controller.RestaurantController;

@Slf4j
@RestControllerAdvice(
    assignableTypes = {
        AddressController.class,
        AuthController.class,
        OrderController.class,
        ProfileController.class,
        AdminOrderController.class,
        CourierController.class,
        RestaurantController.class
    }
)
public class ExceptionHandlerController {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String errors(BusinessLogicException e) {
    log.warn("Ошибка логики: {}", e.getMessage());
    return e.getMessage();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String errors(IllegalArgumentException e) {
    log.warn("Некорректные аргументы: {}", e.getMessage());
    return e.getMessage();
  }

}
