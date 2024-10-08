package gr36.clubActiv.security.sec_filter;


import gr36.clubActiv.security.AuthInfo;
import gr36.clubActiv.security.security_service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

  private final StringHttpMessageConverter stringHttpMessageConverter;
  private TokenService service;

  public TokenFilter(TokenService service, StringHttpMessageConverter stringHttpMessageConverter) {
    this.service = service;
    this.stringHttpMessageConverter = stringHttpMessageConverter;
  }

  // фильтр
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    String token = getTokenFromRequest(httpRequest);

    logger.info("Received token: " + (token != null ? "Token present" : "No token"));

    if (token != null && service.validateAccessToken(token)) {
      logger.info("Token is valid");

      // Получаем claims из токена
      Claims claims = service.getAccessClaims(token);

      // Извлекаем информацию о пользователе из токена
      AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);

      // Устанавливаем аутентификацию в контекст безопасности
      SecurityContextHolder.getContext().setAuthentication(authInfo);

      logger.info("User authenticated: " + authInfo.getName());
    } else {
      logger.warn("Invalid or missing token");
    }

    // Продолжаем цепочку фильтров
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
