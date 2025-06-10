package ru.delivery.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.delivery.dictionary.Role;
import ru.delivery.dictionary.UserStatus;

@Entity
@Table(name = "user")
@Data
@Accessors(chain = true)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGenerator")
  @SequenceGenerator(name = "userGenerator", sequenceName = "s_user", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "row_insert_time", nullable = false, updatable = false)
  private LocalDateTime rowInsertTime;

  @Column(name = "row_update_time", nullable = false)
  private LocalDateTime rowUpdateTime;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @PrePersist
  public void prePersist() {
    rowInsertTime = LocalDateTime.now();
    rowUpdateTime = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    rowUpdateTime = LocalDateTime.now();
  }

  // Реализация UserDetails
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override public String getUsername() { return email; }
  @Override public String getPassword() { return passwordHash; }
  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() {
    return this.getStatus() == UserStatus.ACTIVE;
  }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return this.getStatus() == UserStatus.ACTIVE; }

}