package com.example.springjwt.Service;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 username으로 사용자 조회
        UserEntity userData = userRepository.findByUsername(username);

        if (userData != null) {
            // UserDetails에 담아서 반환
            return new CustomUserDetails(userData);
        }

        // null 반환 금지, 반드시 예외 던지기
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}