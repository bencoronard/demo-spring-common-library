package dev.hireben.demo.common.presentation.utility.annotation.resolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.hireben.demo.common.domain.dto.Paginable;
import dev.hireben.demo.common.presentation.utility.annotation.Pagination;

public class PaginationResolver implements HandlerMethodArgumentResolver {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.getParameterType().equals(Paginable.class) &&
        parameter.hasParameterAnnotation(Pagination.class);
  }

  // ---------------------------------------------------------------------------//

  @Override
  @Nullable
  public Object resolveArgument(
      @NonNull MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) throws Exception {

    Pagination pagination = Optional.ofNullable(parameter.getParameterAnnotation(Pagination.class))
        .orElseThrow(() -> new IllegalStateException("@Pagination not found"));

    String pageParam = webRequest.getParameter("page");
    String sizeParam = webRequest.getParameter("size");
    String[] sortParams = webRequest.getParameterValues("sort");

    int pageNumber = pageParam != null ? Integer.parseInt(pageParam) : pagination.page();
    int pageSize = sizeParam != null ? Integer.parseInt(sizeParam) : pagination.size();
    Collection<String> sortList = sortParams != null ? Arrays.asList(sortParams)
        : Collections.singletonList(pagination.sort());

    Map<String, Boolean> sortFields = sortList.stream()
        .map(param -> param.split(":"))
        .filter(parts -> parts.length == 2)
        .collect(Collectors.toMap(
            parts -> parts[0],
            parts -> "desc".equalsIgnoreCase(parts[1])));

    return Paginable.builder()
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .sortFieldsAsc(sortFields)
        .build();
  }

}
