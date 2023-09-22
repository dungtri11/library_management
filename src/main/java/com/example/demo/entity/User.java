package com.example.demo.entity;

import com.example.demo.common.Authority;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 64, min = 1, message = "User username should less than 64 characters")
    @NotBlank
    @Column(name = "username", nullable = false, length = 64, unique = true)
    private String username;

    @Size(max = 64, min = 1, message = "User password should less than 64 characters")
    @NotBlank
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Size(max = 32, min = 1, message = "User first name should less than 32 characters")
    @NotBlank
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Size(max = 32, min = 1, message = "User last name should less than 32 characters")
    @NotBlank
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @JsonBackReference
    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false, length = 16)
    private Authority authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = List.of(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return authority.toString();
            }
        });
        return grantedAuthorities;
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
