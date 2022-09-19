package org.project.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

/**
 * Kakao API에서 제공하는 사용자 정보 응답 포맷. <a
 * href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#kakaoaccount">링크</a>
 */
@Getter
@ToString
public class KakaoAccount {

  /**
   * 사용자 동의 시 프로필 정보(닉네임/프로필 사진) 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진)
   * <p>
   * 문서에 명시된 타입 : Type
   * <p>
   * Required: false
   */
  @JsonProperty("profile_needs_agreement")
  private Boolean profileNeedsAgreement;

  /**
   * 사용자 동의 시 닉네임 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 닉네임
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("profile_nickname_needs_agreement")
  private Boolean profileNicknameNeedsAgreement;

  /**
   * 사용자 동의 시 프로필 사진 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 사진
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("profile_image_needs_agreement")
  private Boolean profileImageNeedsAgreement;

  /**
   * 프로필 정보
   * <p>
   * <strong>필요한 동의 항목</strong>: 프로필 정보(닉네임/프로필 사진), 닉네임, 프로필 사진
   * <p>
   * 문서에 명시된 타입 : JSON
   * <p>
   * Required: false
   * <p>
   * 참고 : <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#profile">링크</a>
   */
  @JsonProperty("profile")
  private KakaoProfile profile;

  /**
   * 사용자 동의 시 카카오계정 이름 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 이름
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("name_needs_agreement")
  private Boolean nameNeedsAgreement;

  /**
   * 카카오계정 이름
   * <p>
   * <strong>필요한 동의 항목</strong>: 이름
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("name")
  private String name;

  /**
   * 사용자 동의 시 카카오계정 대표 이메일 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(이메일)
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("email_needs_agreement")
  private Boolean emailNeedsAgreement;

  /**
   * 이메일 유효 여부
   * <p>
   * 의미 :
   * <p>
   * - true: 유효한 이메일
   * <p>
   * - false: 이메일이 다른 카카오계정에 사용돼 만료
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(이메일)
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("is_email_valid")
  private Boolean isEmailValid;

  /**
   * 이메일 인증 여부
   * <p>
   * 의미 :
   * <p>
   * - true: 인증된 이메일
   * <p>
   * - false: 인증되지 않은 이메일
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(이메일)
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("is_email_verified")
  private Boolean isEmailVerified;

  /**
   * 카카오계정 대표 이메일
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(이메일)
   * <p>
   * 비고: <a
   * href="https://developers.kakao.com/docs/latest/ko/kakaologin/common#policy-email-caution">이메일
   * 사용 시 주의사항</a>
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("email")
  private String email;

  /**
   * 사용자 동의 시 연령대 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 연령대
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("age_range_needs_agreement")
  private Boolean ageRangeNeedsAgreement;

  /**
   * 연령대
   * <p>
   * 1~9: 1세 이상 10대 미만
   * <p>
   * 10~14: 10세 이상 15세 미만
   * <p>
   * 15~19: 15세 이상 20세 미만
   * <p>
   * 20~29: 20세 이상 30세 미만
   * <p>
   * 30~39: 30세 이상 40세 미만
   * <p>
   * 40~49: 40세 이상 50세 미만
   * <p>
   * 50~59: 50세 이상 60세 미만
   * <p>
   * 60~69: 60세 이상 70세 미만
   * <p>
   * 70~79: 70세 이상 80세 미만
   * <p>
   * 80~89: 80세 이상 90세 미만
   * <p>
   * 90~: 90세 이상
   * <p>
   * <strong>필요한 동의 항목</strong>: 연령대
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("age_range")
  private String ageRange;

  /**
   * 사용자 동의 시 출생 연도 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 출생 연도
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("birthyear_needs_agreement")
  private Boolean birthyearNeedsAgreement;

  /**
   * 출생 연도(YYYY 형식)
   * <p>
   * <strong>필요한 동의 항목</strong>: 출생 연도
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("birthyear")
  private String birthyear;

  /**
   * 사용자 동의 시 생일 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 생일
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("birthday_needs_agreement")
  private Boolean birthdayNeedsAgreement;

  /**
   * 생일(MMDD 형식)
   * <p>
   * <strong>필요한 동의 항목</strong>: 생일
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("birthday")
  private String birthday;

  /**
   * 사용자 동의 시 성별 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 성별
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("gender_needs_agreement")
  private Boolean genderNeedsAgreement;

  /**
   * 성별
   * <p>
   * 의미 :
   * <p>
   * - female: 여성
   * <p>
   * - male: 남성
   * <p>
   * <strong>필요한 동의 항목</strong>: 성별
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("gender")
  private String gender;

  /**
   * 사용자 동의 시 전화번호 제공 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(전화번호)
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("phone_number_needs_agreement")
  private Boolean phoneNumberNeedsAgreement;

  /**
   * 카카오계정의 전화번호
   * <p>
   * 국내 번호인 경우 +82 00-0000-0000 형식
   * <p>
   * 해외 번호인 경우 자릿수, 붙임표(-) 유무나 위치가 다를 수 있음
   * <p>
   * (참고: <a href="https://github.com/google/libphonenumber">libphonenumber</a>)
   * <p>
   * <strong>필요한 동의 항목</strong>: 카카오계정(전화번호)
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("phone_number")
  private String phoneNumber;

  /**
   * 사용자 동의 시 CI 참고 가능
   * <p>
   * <strong>필요한 동의 항목</strong>: CI(연계정보)
   * <p>
   * 문서에 명시된 타입 : Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("ci_needs_agreement")
  private Boolean ciNeedsAgreement;

  /**
   * 연계정보
   * <p>
   * <strong>필요한 동의 항목</strong>: CI(연계정보)
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   */
  @JsonProperty("ci")
  private String ci;

  /**
   * CI 발급 시각, UTC*
   * <p>
   * <strong>필요한 동의 항목</strong>: CI(연계정보)
   * <p>
   * 문서에 명시된 타입 : String
   * <p>
   * Required: false
   * <p>
   * * <a href="https://ko.wikipedia.org/wiki/세계_협정_시각">UTC</a>: 한국 시간(KST)과 9시간 차이, <a
   * href="https://tools.ietf.org/html/rfc3339">RFC3339: Date and Time on the Internet</a> 참고
   */
  @JsonProperty("ci_authenticated_at")
  private Date ciAuthenticatedAt;
}
