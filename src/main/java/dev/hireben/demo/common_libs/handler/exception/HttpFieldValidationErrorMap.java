package dev.hireben.demo.common_libs.handler.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({ "field", "message" })
final class HttpFieldValidationErrorMap {
  String field;
  String message;
}
