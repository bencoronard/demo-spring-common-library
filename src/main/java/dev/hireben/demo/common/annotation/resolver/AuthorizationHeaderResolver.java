package dev.hireben.demo.common.annotation.resolver;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.hireben.demo.common.annotation.AuthorizationHeader;
import dev.hireben.demo.common.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class AuthorizationHeaderResolver implements HandlerMethodArgumentResolver {

  private final JwtUtil jwtUtil;

  // =============================================================================

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(Claims.class)
        && parameter.hasParameterAnnotation(AuthorizationHeader.class);
  }

  // -----------------------------------------------------------------------------

  @Override
  public @Nullable Object resolveArgument(
      MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) throws Exception {

    String header = webRequest.getHeader("Authorization");

    if (header == null || header.isBlank()) {
      throw new MissingRequestHeaderException("Authorization", parameter);
    }

    return jwtUtil.parseToken(header.substring("Bearer ".length()));
  }

}
