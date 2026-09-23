package com.devon.building.security;

import com.devon.building.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OidcUser, UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final User user;
    private final OidcUser oidcUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(User user, OidcUser oidcUser, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.oidcUser = oidcUser;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }

    // UserDetails methods
    @Override
    public String getPassword() {
        return user.getEncrytedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getActive());
    }
}
