package org.project.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 참고: <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#profile">링크</a>
 */
public class KakaoProfile {

  /**
   * 닉네임
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진) 또는 닉네임
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("nickname")
  private String nickname;

  /**
   * 프로필 미리보기 이미지 URL
   * <p>
   * 110px * 110px 또는 100px * 100px
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("thumbnail_image_url")
  private String thumbnailImageUrl;

  /**
   * 프로필 사진 URL
   * <p>
   * 640px * 640px 또는 480px * 480px
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("profile_image_url")
  private String profileImageUrl;

  /**
   * 프로필 사진 URL이 기본 프로필 사진 URL인지 여부
   * <p>
   * 사용자가 등록한 프로필 사진이 없을 경우, 기본 프로필 사진 제공
   * <p>
   * 의미 :
   * <p>
   * - true: 기본 프로필 사진
   * <p>
   * - false: 사용자가 등록한 프로필 사진
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("is_default_image")
  private Boolean isDefaultImage;

}
