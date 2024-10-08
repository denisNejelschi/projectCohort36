package gr36.clubActiv.security.sec_filter;


import gr36.clubActiv.security.AuthInfo;
import gr36.clubActiv.security.security_service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String token = getTokenFromRequest(request);

    if (token != null) {
      try {
        if (service.validateAccessToken(token)) {
          Claims claims = service.getAccessClaims(token);
          AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);
          authInfo.setAuthenticated(true);
          SecurityContextHolder.getContext().setAuthentication(authInfo);
        } else {
          logger.warn("Invalid JWT token");
          ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
          return;
        }
      } catch (Exception e) {
        logger.error("JWT validation error: " + e.getMessage());
        ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token validation error");
        return;
      }
    }


    filterChain.doFilter(servletRequest, servletResponse);
  }


  private String getTokenFromRequest(HttpServletRequest request){
    // заголовок Authorization -> Bearer 2k3424bmn234bm2b34mb2m3b2m2b34
    String token = request.getHeader("Authorization");

    if(token != null && token.startsWith("Bearer ")){
      return token.substring(7);
    }
    return null;
  }
}
