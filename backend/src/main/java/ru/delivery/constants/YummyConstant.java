package ru.delivery.constants;

import java.util.Set;
import ru.delivery.dictionary.OrderStatus;

public class YummyConstant {

  public static class OrderConstant {
    public final static Set<OrderStatus> ACTIVE_STATUSES =
        Set.of(OrderStatus.NEW, OrderStatus.COOKING, OrderStatus.PACKING, OrderStatus.IN_DELIVERY);
  }

}
