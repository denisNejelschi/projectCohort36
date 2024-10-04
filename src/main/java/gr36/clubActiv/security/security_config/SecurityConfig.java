package gr36.clubActiv.security.security_config;

import static org.springframework.security.config.Customizer.withDefaults;

import gr36.clubActiv.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final TokenFilter filter;

  public SecurityConfig(TokenFilter filter) {
    this.filter = filter;
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless JWT authentication
        .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
        .httpBasic(AbstractHttpConfigurer::disable) // Disable basic auth
        .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class) // Custom JWT filter
        .authorizeHttpRequests(x -> x
            .requestMatchers(HttpMethod.GET, "/api/activity").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/activity").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/users/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/activity/update/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/activity/{activity_id}/add-user/{user_id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/activity/user/{userId}/activities").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{activity_id}/remove-user/{user_id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh").permitAll() // Allow login and refresh for everyone
            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll() // Public registration
            .requestMatchers(HttpMethod.GET, "/api/users/register").permitAll() // Public registration page
        ).build();
  }
}



//        .sessionManagement(x -> x
//    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//    .httpBasic(Customizer.withDefaults())