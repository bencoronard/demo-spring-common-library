package dev.hireben.demo.common.presentation.utility.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pagination {
  int page() default 0;

  int size() default 10;

  String sort() default "id:asc";
}
