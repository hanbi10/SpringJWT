package com.example.springjwt.dto;

import com.example.springjwt.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails  implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {

        this.userEntity = userEntity;
    }

    @Override
    // Role값을 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add (new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userEntity.getRole();
            }

        });

        return collection;
    }

    @Override
    // password 값 반환
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    // username 반환
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    // 계정이 expired 되었는가
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
