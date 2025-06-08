package ru.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.CourierStatus;
import ru.delivery.dictionary.Role;
import ru.delivery.dictionary.UserStatus;
import ru.delivery.dto.auth.AuthenticationResponse;
import ru.delivery.dto.auth.WorkerRegisterRequest;
import ru.delivery.entity.Courier;
import ru.delivery.entity.Customer;
import ru.delivery.entity.RestaurantAdmin;
import ru.delivery.entity.User;
import ru.delivery.repository.CourierRepository;
import ru.delivery.repository.CustomerRepository;
import ru.delivery.repository.RestaurantAdminRepository;
import ru.delivery.security.service.JwtService;
import ru.delivery.service.crud.RestaurantCrudService;
import ru.delivery.service.crud.UserCrudService;

@Service
@RequiredArgsConstructor
public class UserService {

  private final static String ROLE_PREFIX = "ROLE_";

  private final UserCrudService userCrudService;
  private final CustomerRepository customerRepository;
  private final RestaurantCrudService restaurantCrudService;
  private final RestaurantAdminRepository restaurantAdminRepository;
  private final CourierRepository courierRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;

  @Transactional
  public AuthenticationResponse register(String email, String password) {
    if (userCrudService.isUserExists(email)) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User()
        .setEmail(email)
        .setPasswordHash(passwordEncoder.encode(password))
        .setRole(Role.ROLE_CUSTOMER)
        .setStatus(UserStatus.ACTIVE);
    user = userCrudService.saveOrUpdate(user);

    Customer customer = new Customer().setUser(user);
    customer = customerRepository.save(customer);

    String jwt = jwtService.generateToken(user);

    return new AuthenticationResponse()
        .setToken(jwt)
        .setUserRole(user.getRole().toValue());
  }

  @Transactional
  public AuthenticationResponse registerWorker(WorkerRegisterRequest request) {
    if (userCrudService.isUserExists(request.getEmail())) {
      throw new RuntimeException("Пользователь уже существует");
    }

    User user = new User()
        .setEmail(request.getEmail())
        .setPasswordHash(passwordEncoder.encode(request.getPassword()))
        .setRole(Role.valueOf(ROLE_PREFIX + request.getRole()))
        .setStatus(UserStatus.ACTIVE);
    user = userCrudService.saveOrUpdate(user);

    createWorker(request,user);

    return new AuthenticationResponse()
        .setToken(jwtService.generateToken(user))
        .setUserRole(user.getRole().toValue());
  }

  private void createWorker(WorkerRegisterRequest request, User user) {
    Role role = Role.valueOf(ROLE_PREFIX + request.getRole());
    var restaurant = restaurantCrudService.getById(request.getRestaurantId());
    if (Role.ROLE_RESTAURANT_ADMIN.equals(role)) {
      var restaurantAdmin = new RestaurantAdmin()
          .setRestaurant(restaurant)
          .setUser(user);
      restaurantAdminRepository.save(restaurantAdmin);
    } else if (Role.ROLE_COURIER.equals(role)) {
      var courier = new Courier()
          .setRestaurant(restaurant)
          .setUser(user)
          .setName(request.getName())
          .setSurname(request.getSurname())
          .setPhone(request.getPhone())
          .setStatus(CourierStatus.BUSY);
      courierRepository.save(courier);
    }
  }

  public AuthenticationResponse login(String email, String password) {

    authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

    var user = userCrudService.getByEmail(email);

    boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPasswordHash());
    if (!isPasswordCorrect) {
      throw new RuntimeException("Введен неправильный пароль");
    }

    return new AuthenticationResponse()
        .setToken(jwtService.generateToken(user))
        .setUserRole(user.getRole().toValue());
  }

}
