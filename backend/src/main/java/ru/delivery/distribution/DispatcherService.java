package ru.delivery.distribution;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import com.google.protobuf.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.delivery.dictionary.CourierStatus;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.entity.Address;
import ru.delivery.entity.Courier;
import ru.delivery.entity.Order;
import ru.delivery.entity.Restaurant;
import ru.delivery.exception.BusinessLogicException;
import ru.delivery.service.crud.CourierCrudService;
import ru.delivery.service.crud.OrderCrudService;
import ru.delivery.service.crud.RestaurantCrudService;
import ru.delivery.utility.GeoUtility;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispatcherService {

  private final static Integer MAX_ORDERS_PER_COURIER = 5;
  private final static Integer DEPOT_INDEX = 0;

  private final OrderCrudService orderCrudService;
  private final RestaurantCrudService restaurantCrudService;
  private final CourierCrudService courierCrudService;

  @Transactional
  public void assignOrdersToCouriers() {
    var restaurants = restaurantCrudService.getAllWithCouriersAndAddresses();
    for (var restaurant : restaurants) {
      assignOrdersToCouriersOfRestaurant(restaurant);
    }
  }

  private void assignOrdersToCouriersOfRestaurant(Restaurant restaurant) {
    var packingOrders = orderCrudService.findByRestaurantAndStatusInPacking(restaurant);
    var couriers = restaurant.getCouriers();
    var freeCouriers = couriers.stream()
        .filter(courier -> CourierStatus.READY.equals(courier.getStatus()))
        .toList();
    if (freeCouriers.isEmpty()) {
      log.info("В ресторане id = {} нет свободных курьеров", restaurant.getId());
      return;
    }

    solveVrp(restaurant, packingOrders, freeCouriers);
  }

  private void solveVrp(
      Restaurant restaurant,
      List<Order> packingOrders,
      List<Courier> freeCouriers) {

// --- Подготовка точек: депо + адреса заказов ---
    // DEPOT — адрес ресторана
    Address depotAddress = restaurant.getAddress();

    List<Address> addresses = new ArrayList<>();
    addresses.add(depotAddress); // индекс 0 — депо
    packingOrders.forEach(order -> addresses.add(order.getCustomerAddress()));

    int numOrders = packingOrders.size();
    int numVehicles = freeCouriers.size();

    // Если суммарная вместимость курьеров меньше заказов — ограничиваем количество заказов, которое можно обслужить
    int maxOrdersCanServe = numVehicles * MAX_ORDERS_PER_COURIER;
    int ordersToAssign = Math.min(numOrders, maxOrdersCanServe);

    long[][] distanceMatrix = initDistanceMatrix(addresses);
    var size = addresses.size();

    // --- Создаем demands — нагрузки ---
    // Для депо = 0, для заказов = 1
    long[] demands = initDemandsArray(ordersToAssign, size);

    // --- Задаем vehicle capacities ---
    long[] vehicleCapacities = initVehicleCapacities(numVehicles);

    RoutingIndexManager manager = new RoutingIndexManager(size, numVehicles, DEPOT_INDEX);
    RoutingModel routing = new RoutingModel(manager);

    // --- Функция расстояния ---
    final int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
      int fromNode = manager.indexToNode(fromIndex);
      int toNode = manager.indexToNode(toIndex);
      return distanceMatrix[fromNode][toNode];
    });

    routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

    // --- Функция demands ---
    final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
      int fromNode = manager.indexToNode(fromIndex);
      return demands[fromNode];
    });

    routing.addDimensionWithVehicleCapacity(
        demandCallbackIndex,
        0,
        vehicleCapacities,
        true,
        "Capacity"
    );

    RoutingSearchParameters searchParameters = main.defaultRoutingSearchParameters()
        .toBuilder()
        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
        .setTimeLimit(Duration.newBuilder().setSeconds(1).build())
        .build();

    log.info("Начинаем решение");
    Assignment solution = routing.solveWithParameters(searchParameters);

    if (solution == null) {
      log.warn(
          "Не удалось найти решение распределения заказов по курьерам в ресторане {}",
          restaurant.getId());
      throw new BusinessLogicException(
          "Не удалось найти решение распределения заказов по курьерам в ресторане %s"
              .formatted(restaurant.getId()));
    }

    // Остальные заказы, не вошедшие в ordersToAssign, остаются без курьера и будут назначены позже.
    doAssignment(
        numVehicles, routing, manager, ordersToAssign, packingOrders, freeCouriers, solution);
  }

  private long[] initVehicleCapacities(int numVehicles) {
    long[] vehicleCapacities = new long[numVehicles];
    for (int i = 0; i < numVehicles; i++) {
      vehicleCapacities[i] = MAX_ORDERS_PER_COURIER;
    }

    return vehicleCapacities;
  }

  private long[] initDemandsArray(int ordersToAssign, int size) {
    long[] demands = new long[size];
    demands[0] = 0;
    for (int i = 1; i <= ordersToAssign; i++) {
      demands[i] = 1;
    }

    return demands;
  }

  private long[][] initDistanceMatrix(List<Address> addresses) {
    int size = addresses.size();
    long[][] distanceMatrix = new long[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          distanceMatrix[i][j] = 0;
        } else {
          distanceMatrix[i][j] = GeoUtility.getDistanceBetweenTwoAddresses(
                  addresses.get(i), addresses.get(j))
              .intValue();
        }
      }
    }

    return distanceMatrix;
  }

  private void doAssignment(
      int numVehicles,
      RoutingModel routing,
      RoutingIndexManager manager,
      int ordersToAssign,
      List<Order> packingOrders,
      List<Courier> freeCouriers,
      Assignment solution) {

    log.info("Создаем назначения");
    for (int vehicleId = 0; vehicleId < numVehicles; vehicleId++) {
      long index = routing.start(vehicleId);
      log.info("Route for courier " + vehicleId + ":");
      List<Order> ordersForCourier = new ArrayList<>();
      while (!routing.isEnd(index)) {
        int nodeIndex = manager.indexToNode(index);
        if (nodeIndex != DEPOT_INDEX && nodeIndex <= ordersToAssign) {
          // Заказ в маршруте курьера (индексы с 1 - offset из packingOrders)
          ordersForCourier.add(packingOrders.get(nodeIndex - 1));
        }
        index = solution.value(routing.nextVar(index));
      }
      int delivery_sequence = 1;
      if (!ordersForCourier.isEmpty()) {
        var courier = freeCouriers.get(vehicleId);
        // Здесь назначаем заказам курьера, например, меняем поле courier и статус заказа
        for (var order : ordersForCourier) {
          order.setCourier(courier);
          order.setStatus(OrderStatus.IN_DELIVERY);
          order.setDeliverySequence(delivery_sequence++);
          orderCrudService.saveOrUpdate(order);
        }
        courier.setStatus(CourierStatus.IN_DELIVERY);
        courierCrudService.saveOrUpdate(courier);
        log.info("Назначено курьеру {} заказов: {}", courier.getId(), ordersForCourier.size());
      }
    }

    // Остальные заказы, не вошедшие в ordersToAssign, остаются без курьера и будут назначены позже.
  }

}
