package com.javadiseno.sanosysalvos.report.security;

import java.util.Collection;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Usuario autenticado vía JWT emitido por User Service (mismo secreto). */
@Getter
public class ReportJwtPrincipal implements UserDetails {

    private final UUID userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public ReportJwtPrincipal(
            UUID userId, String email, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email != null ? email : userId.toString();
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
        return true;
    }
}
