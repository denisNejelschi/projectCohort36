package gr36.clubActiv.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "confirm_code")
public class ConfirmationCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code")
  private String code;

  @Column(name = "expired")
  private LocalDateTime expired;

  @ManyToOne
  private User user;

  public ConfirmationCode() {
  }

  public ConfirmationCode(String code, LocalDateTime expired, User user) {
    this.code = code;
    this.expired = expired;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LocalDateTime getExpired() {
    return expired;
  }

  public void setExpired(LocalDateTime expired) {
    this.expired = expired;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConfirmationCode that)) {
      return false;
    }
    return Objects.equals(getId(), that.getId()) && Objects.equals(getCode(),
        that.getCode()) && Objects.equals(getExpired(), that.getExpired())
        && Objects.equals(getUser(), that.getUser());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCode(), getExpired(), getUser());
  }

  @Override
  public String toString() {
    return String.format("Confirm code: id - %d, code - %s, username - %s, expired - %s",
        id, code, user == null ? "empty" : user.getUsername(), expired);
  }

}
