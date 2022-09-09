package com.example.SpringSecurityJwt.global.security;

import java.time.Duration;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.SpringSecurityJwt.global.config.JwtConfig;
import com.example.SpringSecurityJwt.global.redis.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {
  private final JwtConfig jwtConfig;
  private final RedisService redisService;

  public String createAccessToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
    int expirySeconds = jwtConfig.getExpirySeconds();
    return createToken(userPrincipal.getUsername(), expirySeconds);
  }

  public String createRefreshToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
    int expirySeconds = jwtConfig.getExpirySeconds() * 48 * 14;
    String refreshToken = createToken(userPrincipal.getUsername(), expirySeconds);
    redisService.setValues(userPrincipal.getUsername(), refreshToken, Duration.ofMillis(expirySeconds));
    return refreshToken;
  }

  private String createToken(String username, int expirySeconds) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirySeconds);

    return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(expiryDate)
               .signWith(SignatureAlgorithm.HS512, jwtConfig.getClientSecret())
               .compact();
  }

  public String getUserEmailFromToken(String token) {
    Claims claims = Jwts.parser()
                        .setSigningKey(jwtConfig.getClientSecret())
                        .parseClaimsJws(token)
                        .getBody();

    return claims.getSubject();
  }

  public boolean validateToken(HttpServletRequest request, String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtConfig.getClientSecret()).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      request.setAttribute("exception", "Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      request.setAttribute("exception", "Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      request.setAttribute("exception", "Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      request.setAttribute("exception", "Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      request.setAttribute("exception", "JWT claims string is empty.");
    }

    return false;
  }
}
