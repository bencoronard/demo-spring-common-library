package dev.hireben.demo.common.http.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class ApiAccessLogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!isAsyncDispatch(request)) {
      logger.info(String.format(
          "[id: %s] received %s %s %s",
          request.getRequestId(),
          request.getMethod(),
          request.getRequestURI(),
          request.getProtocol()));
    }

    long start = System.currentTimeMillis();

    try {
      filterChain.doFilter(request, response);
    } finally {

      if (!isAsyncStarted(request)) {
        return;
      }

      logger.info(String.format(
          "[id: %s] responded %d in %dms",
          request.getRequestId(),
          response.getStatus(),
          System.currentTimeMillis() - start));
    }
  }

}
