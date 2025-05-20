package ru.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "address")
@Data
@Accessors(chain = true)
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressGenerator")
  @SequenceGenerator(name = "addressGenerator", sequenceName = "s_address", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "city", nullable = false, length = 50)
  private String city;

  @Column(name = "street", nullable = false, length = 50)
  private String street;

  @Column(name = "building", nullable = false, length = 50)
  private String building;

  @Column(name = "entrance")
  private Integer entrance;

  @Column(name = "floor")
  private Integer floor;

  @Column(name = "apartments")
  private Integer apartments;

  @PrePersist
  public void prePersist() {
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }

}
