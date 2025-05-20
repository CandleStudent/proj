package ru.delivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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

@Entity
@Table(name = "customer")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"user"})
public class Customer {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @OneToOne(optional=false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(referencedColumnName = "id")
  private User user;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "phone", length = 12)
  private String phone;

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<CustomerAddress> addresses = new ArrayList<>();

  public void addAddress(CustomerAddress address) {
    addresses.add(address);
    address.setCustomer(this); // Link the address to this customer
  }

  public void removeAddress(CustomerAddress address) {
    addresses.remove(address);
    address.setCustomer(null); // Unlink the address
  }

  @OneToMany(
      mappedBy = "customer", // Refers to the 'customer' field in Order
      cascade = CascadeType.ALL, // Optional: Persist/delete orders when customer is saved/deleted
      orphanRemoval = true, // Auto-remove orders when removed from this list
      fetch = FetchType.LAZY
  )
  private List<Order> orders = new ArrayList<>();

  public void addOrder(Order order) {
    orders.add(order);
    order.setCustomer(this); // Link the order to this customer
  }

  public void removeOrder(Order order) {
    orders.remove(order);
    order.setCustomer(null); // Unlink the order
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

  public Customer setUser(User user) {
    this.user = user;
    this.id = user.getId();

    return this;
  }

}
