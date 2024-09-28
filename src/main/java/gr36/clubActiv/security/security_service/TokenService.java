package gr36.clubActiv.security.security_service;

import gr36.clubActiv.domain.entity.Role;
import gr36.clubActiv.repository.RoleRepository;
import gr36.clubActiv.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 28/09/2024 clubActiv
 *
 * @author Boris Iurciuc (cohort36)
 */
public class TokenService {
  //fields
  private SecretKey accessKey;
  private SecretKey refreshKey;
  private RoleRepository roleRepository;

  // constructor, в который мы передадим секретные фразы
  // https://www.devglan.com/online-tools/hmac-sha256-online - для генерации секретных фраз
  public TokenService(
      @Value("${key.access}") String accessSecretPhrase,
      @Value("${key.refresh}") String refreshSecretPhrase,
      RoleRepository roleRepository
  ) {
    this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretPhrase));
    this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretPhrase));
    this.roleRepository = roleRepository;
  }

  public String generateAccessToken(UserDetails user) {
    LocalDateTime currentDate = LocalDateTime.now(); // дата сегодня
    Instant expirationInstant = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant(); // дата в будущем на плюс 7 дней от сейчас
    Date expiration = Date.from(expirationInstant); // дата истечения годности токена
    // строим токен
    return Jwts.builder()
        .subject(user.getUsername()) // кладем в токен "предмет" токена
        .expiration(expiration) // кладем дату
        .signWith(accessKey) // кладем ключ
        .claim("roles", user.getAuthorities()) // кладем роли юзера
        .claim("name", user.getUsername()) //
        .compact();
  }

  public String generateRefreshToken(UserDetails user) {
    LocalDateTime currentDate = LocalDateTime.now();
    Instant expirationInstant = currentDate.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
    Date expiration = Date.from(expirationInstant);

    return Jwts.builder()
        .subject(user.getUsername())
        .expiration(expiration)
        .signWith(refreshKey)
        .compact();
  }

  // tokens validation
  public boolean validateAccessToken(String accessToken) {
    return validateToken(accessToken, accessKey);
  }

  public boolean validateRefreshToken(String refreshToken) {
    return validateToken(refreshToken, refreshKey);
  }

  private boolean validateToken(String token, SecretKey key) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false; // токен не валиден
    }
  }

  // методы для извлечения данных из токенов
  public Claims getAccessClaims(String accessToken) {
    return getClaims(accessToken, accessKey);
  }

  public Claims getRefreshClaims(String refreshToken) {
    return getClaims(refreshToken, refreshKey);
  }

  private Claims getClaims(String token, SecretKey key) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // перекладываем полученные из токена данные в тип AuthInfo
  public AuthInfo mapClaimsToAuthInfo(Claims claims) {
    String username = claims.getSubject();
    // List: [
    //           LinkedHashMap: [
    //                              "authority" -> "ROLE_USER"
    //                           ],
    //           LinkedHashMap: [
    //                              "authority" -> "ROLE_ADMIN"
    //                           ]
    //        ]

    List<LinkedHashMap<String, String>> roleList = (List<LinkedHashMap<String, String>>) claims.get("roles");
    Set<Role> roles = new HashSet<>();

    for (LinkedHashMap<String, String> roleEntry : roleList) {
      String roleTitle = roleEntry.get("authority");
      Role role = roleRepository.findByRole(roleTitle).orElseThrow(
          () -> new RuntimeException("Database doesn't contain role")
      );
      roles.add(role);
    }
    return new AuthInfo(username, roles);
  }

}
