package org.project.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

@Service
public class JJwtService implements JwtService {


  @Override
  public String generateToken(Key key, String sub, Date expiration) {
    return Jwts.builder()
        .setSubject(sub)
        .setExpiration(expiration)
        .signWith(key)
        .compact();
  }

  @Override
  public Date getTokenExpirationTime(Key key, String token) throws IllegalArgumentException {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration();
    } catch (ExpiredJwtException e) {
      return e.getClaims().getExpiration();
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }

  @Override
  public String getTokenSub(Key key, String token) throws IllegalArgumentException {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (ExpiredJwtException e) {
      return e.getClaims().getSubject();
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }

  @Override
  public boolean isDateValidForTokenExpClaim(Key key, String token, Date date)
      throws IllegalArgumentException {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getExpiration()
          .after(date);
    } catch (ExpiredJwtException e) {
      return e.getClaims().getExpiration().after(date);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }


  @Override
  public boolean isTokenSignValid(Key key, String token) throws IllegalArgumentException {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      return false;
    } catch (ExpiredJwtException e) {
      return true;
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }
}
