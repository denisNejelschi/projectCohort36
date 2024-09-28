package gr36.clubActiv.security.sec_dto;

import java.util.Objects;

public class RefreshRequestDto {
  //field
  private String refreshToken;

  //getter
  public String getRefreshToken() {
    return refreshToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RefreshRequestDto that)) return false;
    return Objects.equals(refreshToken, that.refreshToken);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(refreshToken);
  }

  @Override
  public String toString() {
    return "Refresh request: refresh token - " + refreshToken;
  }
}
