package ru.delivery.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import ru.delivery.entity.CustomerAddress.CustomerAddressId;

@Entity
@Table(name = "customer_address")
@IdClass(CustomerAddressId.class) // For composite primary key
@Data
@Accessors(chain = true)
public class CustomerAddress {

  @Id
  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "customer_address_fk0"))
  private Customer customer;

  @Id
  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false,
      foreignKey = @ForeignKey(name = "customer_address_fk1"))
  private Address address;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "comment")
  private String comment;

  // Composite ID class
  @Embeddable
  @Getter
  @Setter
  @Data
  public static class CustomerAddressId implements Serializable {
    private Long customer; // matches name of @Id attribute and type of Customer's ID
    private Long address;  // matches name of @Id attribute and type of Address's ID
  }

}