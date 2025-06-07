package ru.delivery.web.api.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.WorkerActiveOrderDto;
import ru.delivery.service.OrderService;

@RestController
@RequestMapping("/api/courier")
@RequiredArgsConstructor
public class CourierController {

  private final OrderService orderService;

  @GetMapping("/active")
  public List<WorkerActiveOrderDto> getActiveOrders(Principal principal) {
    var userEmail = principal.getName();

    var activeOrders = orderService.getCourierActiveOrders(userEmail);
    return activeOrders;
  }

//  @PutMapping("/push/{id}")
//  public WorkerActiveOrderDto pushOrderStatus(Principal principal, @PathVariable Long id) {
//    var userEmail = principal.getName();
//
//    var pushedOrder = orderService.pushOrderStatusByCourier(userEmail, id);
//    return pushedOrder;
//  }
//
//  @PutMapping("/ready")
//  public void setReadyForDeliveryStatus(Principal principal) {
//    var userEmail = principal.getName();
//  }

}
