package ru.delivery.web.api.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WebController {
  @GetMapping("/meow")
  public String getMessage(Principal principal)
  {
    System.out.println(principal.getName());
    return "Spring Boot Application running on Tomcat server!!";
  }
}