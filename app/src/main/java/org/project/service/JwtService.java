package org.project.service;

import java.security.Key;
import java.util.Date;

public interface JwtService {

  /**
   * 주어진 sub와 expiration을 클레임으로 가지고, key로 서명된 jws를 반환
   *
   * @param key        토큰 생성에 사용할 키
   * @param sub        토큰 sub 클레임에 넣을 문자열
   * @param expiration 토큰 exp 클레임에 설정할 만료 시간 Date 객체
   * @return 생성된 jws
   */
  String generateToken(Key key, String sub, Date expiration);

  /**
   * 만료 여부와 상관 없이 주어진 token의 exp에 해당하는 Date객체를 반환한다.
   * <p>
   * 주어진 key의 서명과 맞지 않거나 다른 이유로 유효하지 않은 토큰인 경우 IllegalArgumentException을 던진다.
   *
   * @param key   토큰 검증에 사용할 키
   * @param token 검증할 토큰
   * @return 토큰의 exp 클레임에 해당하는 Date 객체
   * @throws IllegalArgumentException
   */
  Date getTokenExpirationTime(Key key, String token) throws IllegalArgumentException;

  /**
   * 만료 여부와 상관 없이 주어진 token의 sub에 해당하는 문자열을 반환한다.
   * <p>
   * 주어진 key의 서명과 맞지 않거나 다른 이유로 유효하지 않은 토큰인 경우 IllegalArgumentException을 던진다.
   *
   * @param key   토큰 검증에 사용할 키
   * @param token 검증할 토큰
   * @return 토큰의 sub 클레임에 해당하는 문자열
   * @throws IllegalArgumentException
   */
  String getTokenSub(Key key, String token) throws IllegalArgumentException;

  /**
   * 주어진 date가 token의 exp기준에서 유효한 date인지 확인하여 유효하면 true, 아니면 false를 반환한다.
   * <p>
   * 주어진 key의 서명과 맞지 않거나 다른 이유로 유효하지 않은 토큰인 경우 IllegalArgumentException을 던진다.
   * <p>
   * 주어진 date가 exp보다 같지 않고 이전인 경우에만 유효하다.(<a
   * href="https://www.rfc-editor.org/rfc/rfc7519#section-4.1.4">RFC7519-4.1.4.</a>)
   *
   * @param key   토큰 검증에 사용할 키
   * @param token 검증할 토큰
   * @param date  검증할 시간
   * @return 토큰의 exp 클레임에 해당하는 시간이 주어진 date보다 이전이면 true, 그렇지 않으면 false
   * @throws IllegalArgumentException
   */
  boolean isDateValidForTokenExpClaim(Key key, String token, Date date)
      throws IllegalArgumentException;

  /**
   * 만료 여부와 상관 없이 주어진 token의 서명이 주어진 key와 일치하는지 확인한다.
   * <p>
   * 유효 여부와 무관하게 토큰 포맷이 올바르지 않은 경우 IllegalArgumentException을 던진다.
   *
   * @param key   토큰 검증에 사용할 키
   * @param token 검증할 토큰
   * @return 서명이 일치하면 true, 일치하지 않으면 false
   * @throws IllegalArgumentException
   */
  boolean isTokenSignValid(Key key, String token) throws IllegalArgumentException;
}
