package ru.delivery.web.api.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.WorkerActiveOrderDto;
import ru.delivery.service.OrderService;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

  private final OrderService orderService;

  @GetMapping("/active")
  public List<WorkerActiveOrderDto> getActiveOrders(Principal principal) {

    var userEmail = principal.getName();
    var activeOrders = orderService.getActiveOrdersInRestaurant(userEmail);
    return activeOrders;
  }

  @PutMapping("/push/{id}")
  public WorkerActiveOrderDto pushOrderStatus(Principal principal, @PathVariable Long id) {
    var userEmail = principal.getName();

    var pushedOrder = orderService.pushOrderStatusByAdmin(userEmail, id);
    return pushedOrder;
  }

//  @PutMapping("/update/{id}")
//  public void updateOrder(
//      Principal principal,
//      @PathVariable Long id,
//      @Valid @RequestBody UpdatedOrderDto updatedOrderDto) {
//
//    var userEmail = principal.getName();
//
//    orderService.updateOrder(userEmail, id, updatedOrderDto);
//  }
//
//  @DeleteMapping("/cancel/{id}")
//  public void cancelOrder(Principal principal, @PathVariable Long id) {
//    var userEmail = principal.getName();
//    orderService.deleteOrder(userEmail, id);
//  }

}
