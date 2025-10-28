package dev.hireben.demo.common.http.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import dev.hireben.demo.common.constant.MessageHeader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ApiKeyFilter extends OncePerRequestFilter {

  private final String expectedApiKey;

  // =============================================================================

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String reqApiKey = request.getHeader(MessageHeader.API_KEY);

    if (reqApiKey == null || reqApiKey.isBlank()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing API key");
      return;
    }

    if (!reqApiKey.strip().equals(expectedApiKey)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
      return;
    }

    filterChain.doFilter(request, response);
  }

}
