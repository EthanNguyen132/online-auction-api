package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private User user;

  @ManyToOne private Role role;

  public UserRole(User user, Role role) {
    this.user = user;
    this.role = role;
  }
}
