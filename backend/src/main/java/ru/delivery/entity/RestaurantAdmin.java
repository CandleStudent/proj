package ru.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Entity
@Table(name = "restaurant_admin")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"user", "restaurant"})
public class RestaurantAdmin {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @OneToOne(optional=false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(referencedColumnName = "id")
  private User user;

  @OneToOne(optional=false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Restaurant restaurant;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @PrePersist
  public void prePersist() {
    rowInsertTime = LocalDateTime.now();
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }

  public RestaurantAdmin setUser(User user) {
    this.user = user;
    this.id = user.getId();

    return this;
  }

  public RestaurantAdmin setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;

    return this;
  }

}
