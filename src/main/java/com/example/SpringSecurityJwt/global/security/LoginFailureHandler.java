package com.example.SpringSecurityJwt.global.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.example.SpringSecurityJwt.global.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(response.getWriter(),
                                  new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                                                    exception.getMessage()));
  }
}
/*
아래와 같이 예외별로 메세지를 다르게 해서 다른 응답을 보내줄 수 있음

BadCredentialsException: 비밀번호 불일치
UsernameNotFoundException: 계정 없음 (커스텀 예외)
AccountExpiredException: 계정 만료 (커스텀 예외)
CredentialExpiredException: 비밀번호 만료 (커스텀 예외)
DisabledException: 계정 비활성화 (커스텀 예외)
LockedExcpeiton: 계정잠김 (커스텀 예외)
 */
