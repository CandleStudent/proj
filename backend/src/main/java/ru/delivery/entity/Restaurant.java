package ru.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "restaurant")
@Data
@Accessors(chain = true)
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurantGenerator")
  @SequenceGenerator(name = "restaurantGenerator", sequenceName = "s_restaurant", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "comment", length = 255)
  private String comment;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "address_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "restaurant_fk0"))
  private Address address;

  @OneToMany(
      mappedBy = "restaurant",
      cascade = CascadeType.PERSIST,
      fetch = FetchType.LAZY
  )
  private List<Order> orders = new ArrayList<>();

  public Restaurant addOrder(Order order) {
    orders.add(order);
    order.setRestaurant(this);

    return this;
  }

  public Restaurant removeOrder(Order order) {
    if (!this.orders.isEmpty()) {
      orders.remove(order);
      order.setRestaurant(null);
    }

    return this;
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
