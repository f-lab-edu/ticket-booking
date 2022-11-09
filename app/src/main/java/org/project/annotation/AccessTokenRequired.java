package org.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME) // 스프링 aop가 런타임에 어노테이션 정보를 확인 할 수 있도록 설정
@Target(ElementType.METHOD) // 어노테이션을 메소드에만 붙일 수 있도록 설정
public @interface AccessTokenRequired {


}
