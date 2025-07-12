package dev.hireben.demo.common.presentation.configuration;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import dev.hireben.demo.common.presentation.utility.context.RequestAttributeKey;
import dev.hireben.demo.common.presentation.utility.context.RequestUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorAttributesConfig extends DefaultErrorAttributes {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

    // timestamp, status, error, exception, message, errors, trace, path
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

    RequestUtil.getAttribute(webRequest, RequestAttributeKey.TRACE_ID)
        .ifPresent(id -> errorAttributes.put("traceId", id));

    return errorAttributes;
  }

  // ---------------------------------------------------------------------------//

}
