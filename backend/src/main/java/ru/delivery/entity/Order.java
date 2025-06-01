package ru.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.delivery.dictionary.OrderStatus;
import ru.delivery.dictionary.PaymentType;

@Entity
@Table(name = "`order`") // Backticks needed because "order" is a SQL keyword
@Data
@Accessors(chain = true)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderGenerator")
  @SequenceGenerator(name = "orderGenerator", sequenceName = "s_order", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "order_fk0"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "address_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "order_fk1"))
  private Address customerAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "restaurant_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "order_fk2"))
  private Restaurant restaurant;

  @Column(name = "payment_type", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;

  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "cost", precision = 20, scale = 2)
  private BigDecimal cost;

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<OrderItem> items = new ArrayList<>();

  public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
  }

  public void removeItem(OrderItem item) {
    items.remove(item);
    item.setOrder(null);
  }

  public void clearItems() {
    items.clear();
  }

  @PrePersist
  public void prePersist() {
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }
}
