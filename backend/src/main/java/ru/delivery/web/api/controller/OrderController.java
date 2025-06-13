package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.ActiveOrderDto;
import ru.delivery.dto.NewOrderDto;
import ru.delivery.dto.UpdatedOrderDto;
import ru.delivery.service.OrderService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/active")
  public List<ActiveOrderDto> getActiveOrders(Principal principal) {

    var userEmail = principal.getName();

    return orderService.getActiveOrders(userEmail);
  }

  @PostMapping("create")
  public void postOrder(
      Principal principal,
      @Valid @RequestBody NewOrderDto newOrderDto) {

    var userEmail = principal.getName();
    orderService.makeOrder(userEmail, newOrderDto);
  }

  @PutMapping("/update/{id}")
  public void updateOrder(
      Principal principal,
      @PathVariable Long id,
      @Valid @RequestBody UpdatedOrderDto updatedOrderDto) {

    var userEmail = principal.getName();

    orderService.updateOrder(userEmail, id, updatedOrderDto);
  }

  @DeleteMapping("/cancel/{id}")
  public void cancelOrder(Principal principal, @PathVariable Long id) {
    var userEmail = principal.getName();
    orderService.deleteOrder(userEmail, id);
  }

}
