package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String name;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserInfoDto(User user) {
        this.username = user.getUsername();
        this.name = user.getFirstName() + " " + user.getLastName();
        this.grantedAuthorities = user.getAuthorities();
    }
}
