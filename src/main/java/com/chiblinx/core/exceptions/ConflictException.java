package com.chiblinx.core.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class ConflictException extends RuntimeException {

  protected final HttpStatus statusCode = HttpStatus.CONFLICT;


  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
