package com.example.springjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		if ("/login".equals(requestURI) || "/".equals(requestURI) || "/join".equals(requestURI)) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				if (!jwtUtil.isExpired(token)) {
					String username = jwtUtil.getUsername(token);
					String role = jwtUtil.getRole(token);

					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(
									username,
									null,
									List.of(new SimpleGrantedAuthority(role))
							);

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}
} 