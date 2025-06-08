package ru.delivery.web.api.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.CourierInfoDto;
import ru.delivery.dto.WorkerActiveOrderDto;
import ru.delivery.service.CourierService;
import ru.delivery.service.OrderService;

@RestController
@RequestMapping("/api/courier")
@RequiredArgsConstructor
public class CourierController {

  private final OrderService orderService;
  private final CourierService courierService;

  @GetMapping("/order/active")
  public List<WorkerActiveOrderDto> getActiveOrders(Principal principal) {
    var userEmail = principal.getName();

    var activeOrders = orderService.getCourierActiveOrders(userEmail);
    return activeOrders;
  }

  @PutMapping("/order/push/{id}")
  public WorkerActiveOrderDto pushOrderStatus(Principal principal, @PathVariable Long id) {
    var userEmail = principal.getName();

    var pushedOrder = orderService.pushOrderStatusByCourier(userEmail, id);
    return pushedOrder;
  }

  @PutMapping("/status/{status}")
  public void setReadyForDeliveryStatus(Principal principal, @PathVariable String status) {
    var userEmail = principal.getName();

    courierService.setCourierStatus(userEmail, status);
  }

  @GetMapping("/info")
  public CourierInfoDto getCourierInfo(Principal principal) {
    var userEmail = principal.getName();

    return courierService.getCourierInfo(userEmail);
  }

}
