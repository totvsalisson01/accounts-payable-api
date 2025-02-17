package com.totvs.alisson.payable.accounts.config;

import com.totvs.alisson.payable.accounts.security.JwtFilter;
import com.totvs.alisson.payable.accounts.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtFilter jwtFilter;

  public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtFilter jwtFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (authorize) ->
                authorize
                    .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/webjars/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/auth/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/accounts/**"))
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
