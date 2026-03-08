package de.caritas.cob.uploadservice.api.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class TechnicalUserTenantResolverTest {

  public static final long TECHNICAL_CONTEXT = 0L;

  @Mock HttpServletRequest authenticatedRequest;

  @Mock JwtAuthenticationToken token;

  @Mock Jwt accessToken;

  @InjectMocks TechnicalUserTenantResolver technicalUserTenantResolver;

  @BeforeEach
  public void setUp() {
    // Configure deep stubs for token and accessToken so that nested calls work as expected.
    token = Mockito.mock(JwtAuthenticationToken.class, Mockito.RETURNS_DEEP_STUBS);
    accessToken = Mockito.mock(Jwt.class, Mockito.RETURNS_DEEP_STUBS);
  }

  @Test
  void resolve_should_ResolveTechnicalTenantId_ForTechnicalUserRole() {
    // given
    when(authenticatedRequest.getUserPrincipal()).thenReturn(token);
    System.out.println(
        "Principal class: " + authenticatedRequest.getUserPrincipal().getClass().getName());
    when(token.getToken()).thenReturn(accessToken);
    // Simulate a JWT with a "realm_access" claim containing the role "technical"
    when(accessToken.getClaims())
        .thenReturn(
            Maps.newHashMap(
                "realm_access", Maps.newHashMap("roles", Lists.newArrayList("technical"))));

    // when
    var resolved = technicalUserTenantResolver.resolve(authenticatedRequest);

    // then
    assertThat(resolved).contains(TECHNICAL_CONTEXT);
  }

  @Test
  void resolve_should_NotResolveTenantId_When_NonTechnicalUserRole() {
    // given
    when(authenticatedRequest.getUserPrincipal()).thenReturn(token);
    when(token.getToken()).thenReturn(accessToken);
    when(accessToken.getClaims())
        .thenReturn(
            Maps.newHashMap(
                "realm_access", Maps.newHashMap("roles", Lists.newArrayList("non-technical"))));
    // when
    var resolved = technicalUserTenantResolver.resolve(authenticatedRequest);
    // then
    assertThat(resolved).isEmpty();
  }
}
