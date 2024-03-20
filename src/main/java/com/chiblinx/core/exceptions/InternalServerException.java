package com.chiblinx.core.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class InternalServerException extends RuntimeException {

  protected final HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

  public InternalServerException(String message) {
    super(message);
  }

  public InternalServerException(String message, Throwable cause) {
    super(message, cause);
  }

}
