package ru.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_address")
@Data
@Accessors(chain = true)
public class CustomerAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerAddressGenerator")
  @SequenceGenerator(name = "customerAddressGenerator", sequenceName = "s_customer_address", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false,
      foreignKey = @ForeignKey(name = "customer_address_fk0"))
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", nullable = false,
      foreignKey = @ForeignKey(name = "customer_address_fk1"))
  private Address address;

  @Column(name = "comment", length = 255)
  private String comment;
}