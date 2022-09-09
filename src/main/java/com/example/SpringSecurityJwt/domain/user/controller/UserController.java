package com.example.SpringSecurityJwt.domain.user.controller;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringSecurityJwt.domain.user.dto.AuthResponse;
import com.example.SpringSecurityJwt.domain.user.dto.ReIssueRequest;
import com.example.SpringSecurityJwt.domain.user.dto.SignUpRequest;
import com.example.SpringSecurityJwt.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/sign-up")
  public void registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    userService.register(signUpRequest);
  }

  @PostMapping("/re-issue")
  public AuthResponse reIssue(@Valid @RequestBody ReIssueRequest reIssueRequest) {
    return userService.reIssue(reIssueRequest);
  }

  @GetMapping("/test")
  public boolean test() {
    return true;
  }
}
