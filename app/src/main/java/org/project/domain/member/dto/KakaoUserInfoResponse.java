package org.project.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

/**
 * Schema reference from <a
 * href="developers.kakao.com/docs/latest/ko/kakaologin/rest-api">developers.kakao.com/docs/latest/ko/kakaologin/rest-api</a>
 */
@Getter
@ToString
public class KakaoUserInfoResponse {

  /**
   * 회원번호
   * <p>
   * 문서에 명시된 타입: Long
   * <p>
   * Required: true
   */
  private Long id;

  /**
   * 자옹 연결 설정을 비활성화한 경우만 존재
   * <p>
   * 연결하기 호출의 완료 여부
   * <p>
   * 의미 :
   * <p>
   * - false: 연결 대기(Preregistered) 상태
   * <p>
   * - true: 연결(Registered) 상태
   * <p>
   * 문서에 명시된 타입: Boolean
   * <p>
   * Required: false
   */
  @JsonProperty("has_signed_up")
  private Boolean hasSignedUp;

  /**
   * 서비스에 연결 완료된 시각, UTC*
   * <p>
   * * UTC : 한국 시간(KST)과 9시간 차이, <a href="https://www.rfc-editor.org/rfc/rfc3339">RFC3339: Date and
   * Time on the Internet</a> 참고
   * <p>
   * 문서에 명시된 타입: Datetime
   * <p>
   * Required: false
   */
  @JsonProperty("connected_at")
  private Date connectedAt;

  /**
   * 카카오싱크 간편가입을 통해 로그인한 시각, UTC*
   * <p>
   * * UTC : 한국 시간(KST)과 9시간 차이,  <a href="https://www.rfc-editor.org/rfc/rfc3339">RFC3339: Date and
   * Time on the Internet</a> 참고
   * <p>
   * 문서에 명시된 타입: Datetime
   * <p>
   * Required: false
   */
  @JsonProperty("synched_at")
  private Date synchedAt;

  /**
   * 사용자 프로퍼티(Property)
   * <p>
   * <a
   * href="https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite#user-properties">사용자
   * 프로퍼티</a> 참고
   * <p>
   * 문서에 명시된 타입: JSON
   * <p>
   * Required: false
   */
  @JsonProperty("properties")
  private Map<String, Object> properties;

  /**
   * 카카오계정 정보
   * <p>
   * 문서에 명시된 타입: <a
   * href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#kakaoaccount">KakaoAccount</a>
   * <p>
   * Required: false
   */
  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;
}
