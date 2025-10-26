package dev.hireben.demo.common.filter.http;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

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

    String apiKey = request.getHeader("X-Api-Key");

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
