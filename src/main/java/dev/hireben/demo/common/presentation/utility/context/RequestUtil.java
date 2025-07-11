package dev.hireben.demo.common.presentation.utility.context;

import java.util.Optional;

import org.springframework.web.context.request.RequestAttributes;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestUtil {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  public Optional<Object> getAttribute(RequestAttributes requestAttributes, String name) {
    return Optional.ofNullable(requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST));
  }

  // ---------------------------------------------------------------------------//

  public void setAttribute(RequestAttributes requestAttributes, String name, Object value) {
    requestAttributes.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
  }

}
