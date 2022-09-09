package com.example.SpringSecurityJwt.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.SpringSecurityJwt.global.security.AccessDeniedHandlerImpl;
import com.example.SpringSecurityJwt.global.security.CustomAuthenticationEntryPoint;
import com.example.SpringSecurityJwt.global.security.CustomUserDetailsService;
import com.example.SpringSecurityJwt.global.security.LoginFailureHandler;
import com.example.SpringSecurityJwt.global.security.LoginFilter;
import com.example.SpringSecurityJwt.global.security.LoginSuccessHandler;
import com.example.SpringSecurityJwt.global.security.TokenAuthenticationFilter;
import com.example.SpringSecurityJwt.global.security.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private static final String ADMIN = "ADMIN";
  private static final String USER = "USER";

  private final CustomUserDetailsService customUserDetailsService;
  private final TokenProvider tokenProvider;
  private final AuthenticationConfiguration authenticationConfiguration;

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }

  private LoginFilter loginFilter() throws Exception {
    LoginFilter loginFilter = new LoginFilter();
    loginFilter.setFilterProcessesUrl("/api/users/login");
    loginFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
    loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(tokenProvider));
    return loginFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin()
          .disable()
        .csrf()
          .disable()
        .headers()
          .disable()
        .httpBasic()
          .disable()
        .rememberMe()
          .disable()
        .logout()
          .disable()
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .exceptionHandling()
          .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
          .and()
        .exceptionHandling()
          .accessDeniedHandler(new AccessDeniedHandlerImpl())
          .and()
        .authorizeRequests()
          .antMatchers("/api/users/sign-up", "/api/users/login", "/api/users/re-issue")
            .permitAll()
          .antMatchers("/api/users/test").hasAnyRole(USER)
          .antMatchers("/api/users/logout").hasAnyRole(USER, ADMIN)
          .anyRequest().permitAll()
          .and()
        .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
