package ru.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Data
@Accessors(chain = true)
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderItemGenerator")
  @SequenceGenerator(name = "orderItemGenerator", sequenceName = "s_order_item", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "order_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "order_item_fk0"))
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_item_id", nullable = false,
      foreignKey = @ForeignKey(name = "order_item_fk1"))
  private MenuItem menuItem;

  @Column(name = "amount", nullable = false)
  private Integer amount;

  @Column(name = "cost", nullable = false, precision = 20, scale = 2)
  private BigDecimal cost;


  @PrePersist
  public void prePersist() {
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }
}
