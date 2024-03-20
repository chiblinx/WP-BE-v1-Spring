package com.chiblinx.core.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class UnauthorizedException extends RuntimeException {

  protected final HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }
}
