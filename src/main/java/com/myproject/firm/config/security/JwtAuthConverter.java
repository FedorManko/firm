package com.myproject.firm.config.security;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
      new JwtGrantedAuthoritiesConverter();

  @Value("${jwt.auth.converter.principle-attribute}")
  private String principleAttribute;
  @Value("${jwt.auth.converter.resource-id}")
  private String resourceId;

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority> authorities = Stream.concat(
        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
        extractResourceRoles(jwt).stream()
    ).collect(toSet());

    return new JwtAuthenticationToken(
        jwt,
        authorities,
        getPrincipleClaimName(jwt)
    );
  }

  private String getPrincipleClaimName(Jwt jwt) {
    String claimName = JwtClaimNames.SUB;
    if (principleAttribute != null) {
      claimName = principleAttribute;
    }
    return jwt.getClaim(claimName);
  }

  private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

    if (jwt.getClaim("resource_access") == null) {
      return Set.of();
    }
    Map<String, Object> resourceAccess  = jwt.getClaim("resource_access");

    if (resourceAccess.get(resourceId) == null) {
      return Set.of();
    }
    Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);

    Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
    return resourceRoles
        .stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(toSet());
  }
}
