// package dev.hireben.demo.common_libs.http.annotation.resolver;

// import org.springframework.core.MethodParameter;
// import org.springframework.lang.NonNull;
// import org.springframework.lang.Nullable;
// import org.springframework.web.bind.MissingRequestHeaderException;
// import org.springframework.web.bind.support.WebDataBinderFactory;
// import org.springframework.web.context.request.NativeWebRequest;
// import org.springframework.web.method.support.HandlerMethodArgumentResolver;
// import org.springframework.web.method.support.ModelAndViewContainer;

// import dev.hireben.demo.common_libs.constant.MessageHeader;
// import dev.hireben.demo.common_libs.http.annotation.AuthorizationHeader;
// import io.jsonwebtoken.Claims;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// public final class AuthorizationHeaderResolver implements
// HandlerMethodArgumentResolver {

// private final JwtClient jwtClient;

// //
// =============================================================================

// @Override
// public boolean supportsParameter(@NonNull MethodParameter parameter) {
// return parameter.getParameterType().equals(Claims.class)
// && parameter.hasParameterAnnotation(AuthorizationHeader.class);
// }

// //
// -----------------------------------------------------------------------------

// @Override
// public Object resolveArgument(
// @NonNull MethodParameter parameter,
// @Nullable ModelAndViewContainer mavContainer,
// @NonNull NativeWebRequest webRequest,
// @Nullable WebDataBinderFactory binderFactory) throws Exception {

// String header = webRequest.getHeader(MessageHeader.Authorization);

// if (header == null || header.isBlank()) {
// throw new MissingRequestHeaderException(MessageHeader.Authorization,
// parameter);
// }

// return jwtClient.parseToken(header.substring("Bearer ".length()));
// }

// }
