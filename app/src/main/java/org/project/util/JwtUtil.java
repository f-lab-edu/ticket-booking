package org.project.util;

import java.util.Date;

public interface JwtUtil {

  String generateAccessToken(String sub);

  String generateRefreshToken(String sub);

  Date getAccessTokenExpirationTime(String token);

  Date getRefreshTokenExpirationTime(String token);

  String getAccessTokenSub(String token);

  String getRefreshTokenSub(String token);

  boolean isAccessTokenExpired(String token);

  boolean isRefreshTokenExpired(String token);
}