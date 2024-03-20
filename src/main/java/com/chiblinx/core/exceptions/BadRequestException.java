package com.chiblinx.core.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class BadRequestException extends RuntimeException {

  protected final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
