package gr36.clubActiv.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "role")
  private String role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Role role1)) {
      return false;
    }
    return Objects.equals(getId(), role1.getId()) && Objects.equals(role,
        role1.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), role);
  }

  @Override
  public String toString() {
    return String.format("Role: id - %d, title - %s", id, role);
  }

  @Override
  public String getAuthority() {
    return role;
  }
}

