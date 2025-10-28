package dev.hireben.demo.common.http.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import dev.hireben.demo.common.http.constant.RestHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ApiKeyFilter extends OncePerRequestFilter {

  private final String API_KEY_INTERNAL;

  // =============================================================================

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String apiKey = request.getHeader(RestHeader.API_KEY);

    if (apiKey == null || apiKey.isBlank()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing API key");
      return;
    }

    if (!apiKey.strip().equals(API_KEY_INTERNAL)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
      return;
    }

    filterChain.doFilter(request, response);
  }

}
