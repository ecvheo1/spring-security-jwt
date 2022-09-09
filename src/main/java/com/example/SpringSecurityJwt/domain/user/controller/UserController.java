package com.example.SpringSecurityJwt.domain.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringSecurityJwt.domain.user.domain.User;
import com.example.SpringSecurityJwt.domain.user.dto.AuthResponse;
import com.example.SpringSecurityJwt.domain.user.dto.ReIssueRequest;
import com.example.SpringSecurityJwt.domain.user.dto.SignUpRequest;
import com.example.SpringSecurityJwt.domain.user.service.UserService;
import com.example.SpringSecurityJwt.global.security.CurrentUser;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/sign-up")
  @ResponseStatus(HttpStatus.OK)
  public void register(
      @Valid @RequestBody SignUpRequest signUpRequest
  ) {
    signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    userService.register(signUpRequest);
  }

  @PutMapping("/logout")
  public void logout(
      HttpServletRequest request,
      @CurrentUser User user
  ) {
    userService.logout(request, user);
  }

  @PostMapping("/re-issue")
  @ResponseStatus(HttpStatus.OK)
  public AuthResponse reIssue(
      @Valid @RequestBody ReIssueRequest reIssueRequest
  ) {
    return userService.reIssue(reIssueRequest);
  }

  @GetMapping("/test")
  public String test(
      @CurrentUser User user
  ) {
    return user.getEmail();
  }
}
