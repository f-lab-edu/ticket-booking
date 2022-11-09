package org.project.aspect;

import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.project.exception.InvalidAuthorizationHeaderException;
import org.project.exception.TokenRequiredException;
import org.project.service.LoginCommonService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AccessTokenRequiredAspect {

  private final LoginCommonService loginCommonService;

  public AccessTokenRequiredAspect(LoginCommonService loginCommonService) {
    this.loginCommonService = loginCommonService;
  }

  /**
   * 대상 메소드에 `@AccessTokenRequired` 어노테이션을 붙이고, String email 파라미터를 메소드에 추가해주면 적용된다.
   *
   * @param joinPoint
   * @param email
   * @return
   * @throws Throwable
   */
  @Around("@annotation(org.project.annotation.AccessTokenRequired) && args(.., email)")
  public Object accessToken(ProceedingJoinPoint joinPoint, String email) throws Throwable {
    System.out.println("joinPoint = " + joinPoint);
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null) {
      throw new TokenRequiredException();
    } else if (!authHeader.startsWith("Bearer ")) {
      throw new InvalidAuthorizationHeaderException();
    } else {
      String accessToken = authHeader.substring(7);
      loginCommonService.validateAccessToken(accessToken);
      email = loginCommonService.getSubFromAccessToken(accessToken);

      // Replace email argument with email from access token.
      String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
      Object[] args = joinPoint.getArgs();
      String finalEmail = email;
      IntStream.range(0, parameterNames.length)
          .filter(i -> parameterNames[i].equals("email"))
          .forEach(i -> args[i] = finalEmail);
      return joinPoint.proceed(args);
    }
  }
}
