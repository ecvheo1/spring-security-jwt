package com.example.SpringSecurityJwt.global.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.SpringSecurityJwt.global.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenProvider {
  private JwtConfig jwtConfig;

  public TokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String createToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirySeconds());

    return Jwts.builder()
               .setSubject(userPrincipal.getUsername())
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
