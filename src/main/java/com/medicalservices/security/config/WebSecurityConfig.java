package com.medicalservices.security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  {

  private final UserDetailsService userDetailsService;
  private final RsaKeyProperties rsaKeys;

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bcryptPasswordEncoder)
    throws Exception {
    return http
      .getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userDetailsService)
      .passwordEncoder(bcryptPasswordEncoder)
      .and()
      .build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().configurationSource(request -> {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of("*"));
      configuration.setAllowedMethods(List.of("*"));
      configuration.setAllowedHeaders(List.of("*"));
      return configuration;
    });
    http
      .csrf(AbstractHttpConfigurer::disable)

      .authorizeRequests(auth -> auth
        .antMatchers(OPTIONS,"/**").permitAll()
        .antMatchers("/registration/*", "/login")
        .anonymous()
        .antMatchers("/roles/**")
        .hasAuthority("SCOPE_ADMIN")
        .antMatchers("/service_providers/**", "/users/**")
        .hasAnyAuthority("SCOPE_MANAGER", "SCOPE_ADMIN")
        .antMatchers("/logout/test")
        .permitAll()
        .anyRequest().authenticated())

      .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)

      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      .logout(logout -> logout
        .logoutUrl("/logout")
        .deleteCookies().invalidateHttpSession(true)
        .clearAuthentication(true))

      .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
  }

  @Bean
  JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }
}
