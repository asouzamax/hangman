package com.dell.hangman.domain.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private final HttpStatus status;

  public BusinessException(final HttpStatus status, final String message) {
    super(message);
    this.status = status;
  }

  public int getCode() {
    return status.value();
  }

}
