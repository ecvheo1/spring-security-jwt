package com.example.SpringSecurityJwt.domain.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SpringSecurityJwt.domain.user.domain.Role;
import com.example.SpringSecurityJwt.domain.user.domain.User;
import com.example.SpringSecurityJwt.domain.user.domain.UserRepository;
import com.example.SpringSecurityJwt.domain.user.dto.AuthResponse;
import com.example.SpringSecurityJwt.domain.user.dto.ReIssueRequest;
import com.example.SpringSecurityJwt.domain.user.dto.SignUpRequest;
import com.example.SpringSecurityJwt.global.exception.AuthException;
import com.example.SpringSecurityJwt.global.exception.ErrorCode;
import com.example.SpringSecurityJwt.global.security.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final UserRepository userRepository;
  private final TokenProvider tokenProvider;

  public void register(SignUpRequest signUpRequest) {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new AuthException(ErrorCode.DUPLICATED_EMAIL);
    }

    User user = User.builder()
                    .email(signUpRequest.getEmail())
                    .name(signUpRequest.getName())
                    .password(signUpRequest.getPassword())
                    .role(Role.ROLE_ADMIN)
                    .build();

    userRepository.save(user);
  }

  public AuthResponse reIssue(ReIssueRequest reIssueRequest) {
    String email = reIssueRequest.getEmail();
    userRepository.findByEmail(email)
                  .orElseThrow(
                      () -> new UsernameNotFoundException("유저를 찾을 수 없습니다. email: " + email));

    tokenProvider.checkRefreshToken(reIssueRequest.getEmail(), reIssueRequest.getRefreshToken());

    return new AuthResponse(tokenProvider.createAccessToken(email),
                            tokenProvider.createRefreshToken(email));
  }
}
