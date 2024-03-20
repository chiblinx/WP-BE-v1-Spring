package com.chiblinx.core.responses;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSuccessResponse<T> {

  private String message;
  private HttpStatus status = HttpStatus.OK;
  private T data;
  private int statusCode = status.value();
  private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Z"));

  public ApiSuccessResponse(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
    this.statusCode = status.value();
  }

  public ApiSuccessResponse(String message, HttpStatus status, T data) {
    this.message = message;
    this.status = status;
    this.data = data;
    this.statusCode = status.value();
  }
}
