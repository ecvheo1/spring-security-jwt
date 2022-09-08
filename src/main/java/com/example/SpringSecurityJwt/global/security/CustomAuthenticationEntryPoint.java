package com.example.SpringSecurityJwt.global.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 인증되지 않은 사용자의 요청을 처리
 * 예를 들어, 토큰이 없음 or 토큰이 유효하지 않음
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		AuthenticationException e) throws IOException {
		System.out.println(e.getClass().getName());
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
	}
}

