package gr36.clubActiv.security.security_config;

import static org.springframework.security.config.Customizer.withDefaults;

import gr36.clubActiv.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(AbstractHttpConfigurer::disable)
        .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(x -> x

            .requestMatchers(HttpMethod.GET, "/api/activity").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/activity").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/activity/{id}/is-registered").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/activity/user/registered-activities").authenticated()

            // User management: allow ADMIN to update any user and regular users to update themselves
            .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated()

            // News management
            .requestMatchers(HttpMethod.POST, "/api/news").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/news").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/news/{id}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/news/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/news/{id}").hasRole("ADMIN")

            .requestMatchers(HttpMethod.PUT, "/api/activity/update/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/activity/{activity_id}/add-user").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/activity/my-activities").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{activity_id}/remove-user").hasAnyRole("ADMIN", "USER")

            //Reviews
            .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/reviews").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/reviews/{id}").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/reviews/{id}").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/api/reviews/{id}").hasAnyRole("ADMIN", "USER")

            //Responses
            .requestMatchers(HttpMethod.POST, "/api/review/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/api/responses/review/{reviewId}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/review").permitAll()


            .requestMatchers(HttpMethod.PUT, "/api/activity/update/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{id}").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.PUT, "/api/activity/{activity_id}/add-user").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.GET, "/api/activity/my-activities").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.DELETE, "/api/activity/{activity_id}/remove-user")
            .authenticated()

            // Authentication and registration routes
            .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/refresh").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/auth/logout").authenticated()

        ).build();
  }

}
