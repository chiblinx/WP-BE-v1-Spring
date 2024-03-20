package com.chiblinx.core.responses;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiErrorResponse {

  private Object errors = "Something Went Wrong";
  private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
  private int statusCode = status.value();
  private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Z"));

  public ApiErrorResponse(Object _errors) {
    this.errors = _errors;
  }

  public ApiErrorResponse(Object _errors, HttpStatus status) {
    this.errors = _errors;
    this.status = status;
    this.statusCode = status.value();
  }

  public Object getErrors() {
    return errors != null ? errors : "Something Went Wrong";
  }
}
