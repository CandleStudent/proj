package ru.delivery.service;

import static ru.delivery.utility.GeoUtility.getDistanceBetweenTwoAddresses;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.dictionary.PaymentType;
import ru.delivery.dto.ActiveOrderDto;
import ru.delivery.dto.MenuItemDto;
import ru.delivery.dto.NewOrderDto;
import ru.delivery.dto.UpdatedOrderDto;
import ru.delivery.dto.WorkerActiveOrderDto;
import ru.delivery.entity.Address;
import ru.delivery.entity.Customer;
import ru.delivery.entity.Order;
import ru.delivery.entity.OrderItem;
import ru.delivery.entity.Restaurant;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.mapper.AddressMapper;
import ru.delivery.mapper.OrderMapper;
import ru.delivery.service.crud.AddressCrudService;
import ru.delivery.service.crud.CourierCrudService;
import ru.delivery.service.crud.CustomerCrudService;
import ru.delivery.service.crud.MenuItemCrudService;
import ru.delivery.service.crud.OrderCrudService;
import ru.delivery.service.crud.RestaurantAdminCrudService;
import ru.delivery.service.crud.RestaurantCrudService;

@Service
@RequiredArgsConstructor

public class OrderService {

  private final CustomerCrudService customerCrudService;
  private final AddressMapper addressMapper;
  private final OrderMapper orderMapper;
  private final AddressCrudService addressCrudService;
  private final MenuItemCrudService menuItemCrudService;
  private final OrderCrudService orderCrudService;
  private final RestaurantCrudService restaurantCrudService;
  private final RestaurantAdminCrudService restaurantAdminCrudService;
  private final CourierCrudService courierCrudService;


  @Transactional
  public void makeOrder(String userEmail, @Valid NewOrderDto newOrderDto) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    var address = addressCrudService.getById(newOrderDto.getCustomerAddressId());
    if (!customer.getAddresses().contains(address)) {
      throw new BusinessLogicException("У клиента нет адреса с таким id");
    }
    var restaurant = chooseRestaurantForOrder(address);

    var order = new Order();
    order = setOrderDtoDataToOrder(order, newOrderDto, customer, address, restaurant);

    orderCrudService.saveOrUpdate(order);
  }

  private Restaurant chooseRestaurantForOrder(Address address) {
    var restaurants = restaurantCrudService.getAllWithAddresses();
    Restaurant closestRestaurant = null;
    Double closestDistance = Double.MAX_VALUE;
    for (var restaurant : restaurants) {
      var currentDistance = getDistanceBetweenTwoAddresses(address, restaurant.getAddress());
      if (currentDistance < closestDistance) {
        closestDistance = currentDistance;
        closestRestaurant = restaurant;
      }
    }

    return closestRestaurant;
  }

  private Order setOrderDtoDataToOrder(
      Order order,
      NewOrderDto newOrderDto,
      Customer customer,
      Address address,
      Restaurant restaurant) {

    order.setCustomer(customer)
        .setCustomerAddress(address)
        .setPaymentType(PaymentType.valueOf(newOrderDto.getPaymentType()))
        .setStatus(OrderStatus.NEW)
        .setRestaurant(restaurant);

    order = setOrderContentFromDto(order, newOrderDto.getMenuItems());

    return order;
  }

  private Order setOrderContentFromDto(Order order, List<MenuItemDto> menuItems) {
    BigDecimal cost = BigDecimal.ZERO;
    for (var item : menuItems) {

      var menuItem = menuItemCrudService.getById(item.getId());

      OrderItem orderItem = new OrderItem()
          .setOrder(order)
          .setCost(menuItem.getCost())
          .setAmount(item.getAmount())
          .setMenuItem(menuItem);

      order.addItem(orderItem);

      cost = cost.add(orderItem.getCost().multiply(BigDecimal.valueOf(orderItem.getAmount())));
    }
    order.setCost(cost);

    return order;
  }

  @Transactional
  public List<ActiveOrderDto> getActiveOrders(String userEmail) {
    var customer = customerCrudService.getByEmailWithAddresses(userEmail);

    //todo replace with orderMapper
    return customer.getOrders()
        .stream()
        .filter(order -> !order.getStatus().equals(OrderStatus.DONE))
        .map(order -> new ActiveOrderDto()
            .setId(order.getId())
            .setAddress(addressMapper.addressToAddressDto(order.getCustomerAddress()))
            .setPaymentType(order.getPaymentType().toValue())
            .setStatus(order.getStatus().getStatusDescription())
            .setCost(order.getCost())
            .setRestaurantId(order.getRestaurant().getId())
            .setRestaurantFormattedAddress(
                order.getRestaurant().getAddress().getFormattedAddress())
            .setCustomerFormattedAddress(order.getCustomerAddress().getFormattedAddress())
            .setMenuItems(
                order.getItems()
                    .stream()
                    .map(
                        orderItem -> new MenuItemDto()
                            .setAmount(orderItem.getAmount())
                            .setId(orderItem.getMenuItem().getId()))
                    .toList()
            ))
        .toList();
  }

  @Transactional
  public void deleteOrder(String userEmail, Long id) {
    var customer = customerCrudService.getByEmailWithOrders(userEmail);

    var deletingOrder = customer.getOrders().stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "Вы пытаетесь отменить заказ, который не делали"));
    if (deletingOrder.getStatus().equals(OrderStatus.IN_DELIVERY)
        || deletingOrder.getStatus().equals(OrderStatus.DONE)) {
      throw new BusinessLogicException("Отменить заказ можно только до поступления в доставку");
    }

    customer.getOrders().remove(deletingOrder);

    customerCrudService.saveOrUpdate(customer);
  }

  @Transactional
  public void updateOrder(String userEmail, Long id, @Valid UpdatedOrderDto updatedOrderDto) {
    var customer = customerCrudService.getByEmailWithOrders(userEmail);
    var updatingOrder = customer.getOrders().stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "Вы пытаетесь изменить заказ, который не делали"));

    if (!OrderStatus.NEW.equals(updatingOrder.getStatus())) {
      throw new BusinessLogicException("Заказ уже готовится, его нельзя обновить");
    }

    updatingOrder.clearItems();

    updatingOrder = setOrderContentFromDto(updatingOrder, updatedOrderDto.getMenuItems());
    orderCrudService.saveOrUpdate(updatingOrder);
  }

  @Transactional
  public List<WorkerActiveOrderDto> getActiveOrdersInRestaurant(String userEmail) {
    var admin = restaurantAdminCrudService.getByEmail(userEmail);
    var activeOrders = orderCrudService.findByRestaurantAndStatusIn(admin.getRestaurant());

    return orderMapper.ordersToWorkerActiveOrderDtos(activeOrders);
  }

  @Transactional
  public WorkerActiveOrderDto pushOrderStatusByAdmin(String userEmail, Long id) {
    var admin = restaurantAdminCrudService.getByEmail(userEmail);
    var activeOrders = orderCrudService.findByRestaurantAndStatusIn(admin.getRestaurant());

    var pushedOrder = activeOrders.stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "У ресторана нет активного заказа с id = %s".formatted(id)));
    if (OrderStatus.DONE.equals(pushedOrder.getStatus())) {
      throw new BusinessLogicException("Заказ уже выполнен, его статус нельзя поменять");
    }

    var newStatus = OrderStatus.fromOrderNumber(pushedOrder.getStatus().getOrderNumber() + 1);
    pushedOrder.setStatus(newStatus);

    orderCrudService.saveOrUpdate(pushedOrder);
    return orderMapper.orderToWorkerActiveOrderDto(pushedOrder);
  }

  @Transactional
  public void deleteOrderByAdmin(String userEmail, Long id) {
    var admin = restaurantAdminCrudService.getByEmail(userEmail);
    var activeOrders = orderCrudService.findByRestaurantAndStatusIn(admin.getRestaurant());

    var deletingOrder = activeOrders.stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "В данном ресторане нет активного заказа с id = %s".formatted(id)));
    if (deletingOrder.getStatus().equals(OrderStatus.DONE)) {
      throw new BusinessLogicException("Заказ id = %s завершен, его нельзя удалить".formatted(id));
    }

    orderCrudService.delete(deletingOrder);
  }

  @Transactional
  public void updateOrderByAdmin(String userEmail, Long id, @Valid UpdatedOrderDto updatedOrderDto) {
    var admin = restaurantAdminCrudService.getByEmail(userEmail);
    var activeOrders = orderCrudService.findByRestaurantAndStatusIn(admin.getRestaurant());
    
    var updatingOrder = activeOrders.stream()
        .filter(order -> order.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new BusinessLogicException(
            "В данном ресторане нет активного заказа с id = %s".formatted(id)));

    if (updatingOrder.getStatus().getOrderNumber() > 2) {
      throw new BusinessLogicException("Заказ уже приготовлен, его нельзя обновить");
    }

    updatingOrder.clearItems();

    updatingOrder = setOrderContentFromDto(updatingOrder, updatedOrderDto.getMenuItems());
    orderCrudService.saveOrUpdate(updatingOrder);
    
  }

  @Transactional
  public List<WorkerActiveOrderDto> getCourierActiveOrders(String userEmail) {
    var courier = courierCrudService.getByEmail(userEmail);
    var activeOrders = orderCrudService.findByCourierAndStatusIn(courier);

    return orderMapper.ordersToWorkerActiveOrderDtos(activeOrders);
  }
}
