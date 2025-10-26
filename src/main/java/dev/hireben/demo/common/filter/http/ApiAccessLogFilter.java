package dev.hireben.demo.common.filter.http;

import org.springframework.web.filter.AbstractRequestLoggingFilter;
import jakarta.servlet.http.HttpServletRequest;

public final class ApiAccessLogFilter extends AbstractRequestLoggingFilter {

  @Override
  protected void beforeRequest(HttpServletRequest request, String message) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'beforeRequest'");
  }

  // -----------------------------------------------------------------------------

  @Override
  protected void afterRequest(HttpServletRequest request, String message) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'afterRequest'");
  }

}
