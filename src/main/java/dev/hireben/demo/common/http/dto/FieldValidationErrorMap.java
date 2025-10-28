package dev.hireben.demo.common.http.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({ "field", "message" })
public final class FieldValidationErrorMap {
  String field;
  String message;
}
