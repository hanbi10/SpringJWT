package com.example.springjwt.jwt;

import com.example.springjwt.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // JSON 요청 처리
        if (username == null || password == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                // JSON 요청을 Map으로 변환
                java.util.Map<String, String> jsonRequest = mapper.readValue(request.getInputStream(), java.util.Map.class);
                username = jsonRequest.get("username");
                password = jsonRequest.get("password");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // 인증 토큰 생성 (권한 null)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        // AuthenticationManager를 통해 인증 수행
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 권한 확인
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? "USER" : authorities.iterator().next().getAuthority();

        // JWT 생성 (10시간 유효)
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L);

        // Authorization 헤더에 JWT 추가
        response.addHeader("Authorization", "Bearer " + token);

        // JSON 형식으로 로그인 성공 메시지 반환 (선택)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"success\", \"token\": \"" + token + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": \"fail\", \"message\": \"Authentication failed\"}");
    }
}