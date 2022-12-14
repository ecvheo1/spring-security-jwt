package com.example.SpringSecurityJwt.global.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.SpringSecurityJwt.domain.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		new ObjectMapper().writeValue(response.getWriter(), new AuthResponse(
				tokenProvider.createAccessToken(userPrincipal.getUsername()),
				tokenProvider.createRefreshToken(userPrincipal.getUsername())));
	}
}
