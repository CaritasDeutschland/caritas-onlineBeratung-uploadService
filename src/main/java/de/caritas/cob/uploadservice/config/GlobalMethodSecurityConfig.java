package de.caritas.cob.uploadservice.config;

import de.caritas.cob.uploadservice.api.authorization.Authority.AuthorityValue;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
public class GlobalMethodSecurityConfig {

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
    expressionAdvice.setExpressionHandler(methodSecurityExpressionHandler());

    RoleVoter roleVoter = new RoleVoter();
    roleVoter.setRolePrefix(""); // Remove default "ROLE_" prefix

    List<AccessDecisionVoter<?>> decisionVoters = List.of(roleVoter, new AuthenticatedVoter());

    return new AffirmativeBased(decisionVoters);
  }

  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(AuthorityValue.PREFIX);
  }

  @Bean
  public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
    return new DefaultMethodSecurityExpressionHandler();
  }
}
