package org.project.aspect;

import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.project.domain.Member;
import org.project.exception.InvalidAccessTokenException;
import org.project.exception.InvalidAuthorizationHeaderException;
import org.project.exception.TokenRequiredException;
import org.project.repository.MemberRepository;
import org.project.service.LoginCommonService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AccessTokenRequiredAspect {

  private final LoginCommonService loginCommonService;
  private final MemberRepository memberRepository;

  public AccessTokenRequiredAspect(LoginCommonService loginCommonService,
      MemberRepository memberRepository) {
    this.loginCommonService = loginCommonService;
    this.memberRepository = memberRepository;
  }

  /**
   * 대상 메소드에 `@AccessTokenRequired` 어노테이션을 붙이고, `Member member` 파라미터를 메소드 맨 앞에 추가해주면 적용된다. (member가
   * 뒤에 와야 한다.)
   *
   * @param joinPoint
   * @param member
   * @return
   * @throws Throwable
   */
  @Around("@annotation(org.project.annotation.AccessTokenRequired) && args(member,..)")
  public Object accessToken(ProceedingJoinPoint joinPoint, Member member)
      throws Throwable {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    String authHeader = request.getHeader("Authorization");
    validateAuthHeader(authHeader);

    String accessToken = authHeader.substring(7);
    loginCommonService.validateAccessToken(accessToken);
    String email = loginCommonService.getSubFromAccessToken(accessToken);

    // 이메일로 회원을 조회해서 파라미터로 넘어온 Member 객체에 넣어준다.
    Member findMember = memberRepository.findByEmail(email)
        .orElseThrow(InvalidAccessTokenException::new);

    // Replace Member argument with Member from email.
    String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    Object[] args = joinPoint.getArgs();
    IntStream.range(0, parameterNames.length)
        .filter(i -> parameterNames[i].equals("member"))
        .forEach(i -> args[i] = findMember);
    return joinPoint.proceed(args);
  }

  private static void validateAuthHeader(String authHeader) {
    if (authHeader == null) {
      throw new TokenRequiredException();
    } else if (!authHeader.startsWith("Bearer ")) {
      throw new InvalidAuthorizationHeaderException();
    }
  }
}
