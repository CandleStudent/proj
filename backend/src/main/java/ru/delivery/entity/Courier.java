package ru.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.delivery.dictionary.CourierStatus;

@Entity
@Table(name = "courier")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"user", "restaurant"})
public class Courier {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @OneToOne(optional=false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(referencedColumnName = "id")
  private User user;

  @ManyToOne(optional=false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(referencedColumnName = "id")
  private Restaurant restaurant;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "phone", length = 13)
  private String phone;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private CourierStatus status;

  @OneToMany(
      mappedBy = "courier",
      cascade = CascadeType.PERSIST,
      fetch = FetchType.LAZY
  )
  private List<Order> orders = new ArrayList<>();

  public Courier addOrder(Order order) {
    orders.add(order);
    order.setCourier(this);

    return this;
  }

  public Courier removeOrder(Order order) {
    if (!this.orders.isEmpty()) {
      orders.remove(order);
      order.setCourier(null);
    }

    return this;
  }

  @PrePersist
  public void prePersist() {
    rowInsertTime = LocalDateTime.now();
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }

  public Courier setUser(User user) {
    this.user = user;
    this.id = user.getId();

    return this;
  }


  //todo здесь и в прочих местах обновлять обе стороны связи
  // todo как и в удалении, так и в добавлении, в т.ч. в списках
  public Courier setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;

    return this;
  }

}
