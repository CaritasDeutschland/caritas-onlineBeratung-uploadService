package de.caritas.cob.uploadservice.api.authorization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class RoleAuthorizationAuthorityMapperTest {

  private final Set<String> roles =
      Stream.of(UserRole.values()).map(UserRole::getValue).collect(Collectors.toSet());

  @Test
  void roleAuthorizationAuthorityMapper_Should_GrantCorrectAuthorities() {

    RoleAuthorizationAuthorityMapper roleAuthorizationAuthorityMapper =
        new RoleAuthorizationAuthorityMapper();

    var result = roleAuthorizationAuthorityMapper.mapAuthorities(roles);

    Set<SimpleGrantedAuthority> expectedGrantendAuthorities = new HashSet<>();
    roles.forEach(
        roleName -> {
          expectedGrantendAuthorities.addAll(
              Authority.getAuthoritiesByUserRole(UserRole.getRoleByValue(roleName).get()).stream()
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toSet()));
        });

    assertThat(expectedGrantendAuthorities).isEqualTo(result);
  }
}
