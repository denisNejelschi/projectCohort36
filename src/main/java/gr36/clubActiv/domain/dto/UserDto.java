package gr36.clubActiv.domain.dto;

import java.util.Objects;

public class UserDto {

  private Long id;

  private String username;

  private String email;

  private String password;

  private String image;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDto userDto)) {
      return false;
    }
    return Objects.equals(getId(), userDto.getId()) && Objects.equals(
        getUsername(), userDto.getUsername()) && Objects.equals(getEmail(),
        userDto.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUsername(), getEmail());
  }

  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
