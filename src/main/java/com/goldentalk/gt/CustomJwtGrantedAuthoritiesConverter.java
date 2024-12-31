package com.goldentalk.gt;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @NotEmpty
    private List<String> clientIds;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        return new JwtAuthenticationToken(source, Stream.concat(new JwtGrantedAuthoritiesConverter().convert(source)
                        .stream(), extractResourceRoles(source).stream())
                .collect(toSet()));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        var resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var resourceRoles = new ArrayList<>();

        clientIds.stream().forEach(id ->
        {
            if (resourceAccess.containsKey(id)) {
                var resource = (Map<String, List<String>>) resourceAccess.get(id);
                resource.get("roles").forEach(role -> resourceRoles.add(id + "_" + role));
            }
        });
        return resourceRoles.isEmpty() ? emptySet() : resourceRoles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(toSet());
    }
    /*private static final String ROLES_CLAIM = "roles"; // Replace with the claim key from your JWT

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract roles from the custom "roles" claim
        List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);

        // Convert roles into GrantedAuthority objects
        Collection<GrantedAuthority> authorities = roles != null
                ? roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList())
                : List.of();

        // Add default authorities provided by JwtGrantedAuthoritiesConverter
        authorities.addAll(super.convert(jwt));
        return authorities;
    }*/


}
