package com.dell.hangman.domain.exception;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends BusinessException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public GameNotFoundException(String gameId) {
    super(HttpStatus.NOT_FOUND, String.format("Game with id '%s' not found", gameId));
  }

}
