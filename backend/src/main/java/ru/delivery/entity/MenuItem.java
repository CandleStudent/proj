package ru.delivery.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_item")
@Data
@Accessors(chain = true)
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuItemGenerator")
  @SequenceGenerator(name = "menuItemGenerator", sequenceName = "s_menu_item", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "name", nullable = false, unique = true, length = 50)
  private String name;

  @Column(name = "category", nullable = false, length = 50)
  private String category;

  @Column(name = "cost", nullable = false, precision = 20, scale = 2)
  private BigDecimal cost;

  @Column(name = "ingredients")
  private String ingredients;

  @Column(name = "description")
  private String description;

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