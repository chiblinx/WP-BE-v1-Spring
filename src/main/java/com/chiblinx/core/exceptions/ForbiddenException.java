package com.chiblinx.core.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class ForbiddenException extends RuntimeException {

  protected final HttpStatus statusCode = HttpStatus.FORBIDDEN;

  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }
}
