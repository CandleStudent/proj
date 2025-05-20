package ru.delivery.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

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

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "order_fk0"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_address_id", nullable = false,
      foreignKey = @ForeignKey(name = "order_fk1"))
  private CustomerAddress customerAddress;

  @Column(name = "is_delivery", nullable = false)
  private Boolean isDelivery;

  @Column(name = "payment_type", nullable = false, length = 20)
  private String paymentType;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "cost", precision = 20, scale = 2)
  private BigDecimal cost;

  @PrePersist
  public void prePersist() {
    rowInsertTime = LocalDateTime.now();
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }
}
