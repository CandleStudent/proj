package ru.delivery.constants;

import java.util.Set;
import lombok.experimental.UtilityClass;
import ru.delivery.dictionary.OrderStatus;

@UtilityClass
public class YummyConstant {

  @UtilityClass
  public static class OrderConstant {

    public final static Set<OrderStatus> ACTIVE_STATUSES =
        Set.of(OrderStatus.NEW, OrderStatus.COOKING, OrderStatus.PACKING, OrderStatus.IN_DELIVERY);

    public static final Set<OrderStatus> PACKING_STATUS_SET = Set.of(OrderStatus.PACKING);

    public static final Set<OrderStatus> IN_DELIVERY_STATUS_SET = Set.of(OrderStatus.IN_DELIVERY);
  }

}
