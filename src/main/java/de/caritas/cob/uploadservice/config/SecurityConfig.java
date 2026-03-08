package de.caritas.cob.uploadservice.config;

import static de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue.ANONYMOUS_DEFAULT;
import static de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue.CONSULTANT_DEFAULT;
import static de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue.TECHNICAL_DEFAULT;
import static de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue.USER_DEFAULT;
import static de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue.USE_FEEDBACK;

import de.caritas.cob.uploadservice.filter.HttpTenantFilter;
import de.caritas.cob.uploadservice.filter.StatelessCsrfFilter;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@KeycloakConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

  @Value("${csrf.cookie.property}")
  private String csrfCookieProperty;

  @Value("${csrf.header.property}")
  private String csrfHeaderProperty;

  @Value("${csrf.whitelist.header.property}")
  private String csrfWhitelistHeaderProperty;

  @Autowired(required = false)
  private HttpTenantFilter httpTenantFilter;

  @Value("${multitenancy.enabled}")
  private boolean multitenancy;

  // Whitelist for Swagger, Actuator endpoints, etc.
  public static final String[] WHITE_LIST = {
    "/uploads/docs",
    "/uploads/docs/**",
    "/v2/api-docs",
    "/configuration/ui",
    "/swagger-resources/**",
    "/configuration/security",
    "/swagger-ui",
    "/swagger-ui/**",
    "/webjars/**",
    "/actuator/health",
    "/actuator/health/**"
  };

  @Autowired AuthorisationService authorisationService;

  @Autowired JwtAuthConverterProperties jwtAuthConverterProperties;

  /** Defines our SecurityFilterChain (the new style in Spring Security 6). */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable())
        .addFilterBefore(
            new StatelessCsrfFilter(
                csrfCookieProperty, csrfHeaderProperty, csrfWhitelistHeaderProperty),
            CsrfFilter.class);

    if (multitenancy && httpTenantFilter != null) {
      http.addFilterAfter(httpTenantFilter, BearerTokenAuthenticationFilter.class);
    }

    http.sessionManagement(
        session ->
            session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()));

    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(WHITE_LIST)
                    .permitAll()
                    .requestMatchers("/uploads/messages/key")
                    .hasAuthority(TECHNICAL_DEFAULT)
                    .requestMatchers("/uploads/new/{roomId:[0-9A-Za-z]+}")
                    .hasAnyAuthority(USER_DEFAULT, CONSULTANT_DEFAULT, ANONYMOUS_DEFAULT)
                    .requestMatchers("/uploads/feedback/new/{feedbackRoomId:[0-9A-Za-z]+}")
                    .hasAuthority(USE_FEEDBACK)
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())));
    return http.build();
  }

  @Bean
  public JwtAuthConverter jwtAuthConverter() {
    return new JwtAuthConverter(jwtAuthConverterProperties, authorisationService);
  }

  /** Use a NullAuthenticatedSessionStrategy for stateless sessions. */
  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }
}
