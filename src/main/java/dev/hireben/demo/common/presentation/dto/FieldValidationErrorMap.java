package dev.hireben.demo.common.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({ "field", "message" })
public class FieldValidationErrorMap {

  // ---------------------------------------------------------------------------//
  // Fields
  // ---------------------------------------------------------------------------//

  String field;
  String message;

}
