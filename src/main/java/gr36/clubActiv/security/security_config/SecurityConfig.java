package gr36.clubActiv.security.security_config;

import static org.springframework.security.config.Customizer.withDefaults;

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

  @Bean
  public BCryptPasswordEncoder encoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer :: disable)
        .sessionManagement(x -> x
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // отключили сессии
        .httpBasic(AbstractHttpConfigurer::disable) // отключили базовую авторизацию
        .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class) // добавили свой фильтр
        .authorizeHttpRequests(x -> x
            .requestMatchers(HttpMethod.GET, "/api/activity").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/activity").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/activity/update/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll() // разрешаем всем доступ к этим endpoints
        ).build();
  }
}

//        .sessionManagement(x -> x
//    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//    .httpBasic(Customizer.withDefaults())