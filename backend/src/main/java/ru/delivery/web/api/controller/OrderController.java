package ru.delivery.web.api.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.delivery.dto.NewOrderDto;
import ru.delivery.service.OrderService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/active")
  public ResponseEntity<?> getActiveOrders(Principal principal) {

    var userEmail = principal.getName();

    try {
      var activeOrders = orderService.getActiveOrders(userEmail);
      return ResponseEntity.ok(activeOrders);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("create")
  public ResponseEntity<?> postOrder(
      Principal principal,
      @Valid @RequestBody NewOrderDto newOrderDto) {

    var userEmail = principal.getName();

    try {
      orderService.makeOrder(userEmail, newOrderDto);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
