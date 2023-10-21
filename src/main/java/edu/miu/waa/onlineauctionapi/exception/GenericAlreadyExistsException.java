package edu.miu.waa.onlineauctionapi.exception;

import java.io.Serial;

public class GenericAlreadyExistsException extends RuntimeException {

  @Serial private static final long serialVersionUID = 3946919557745116819L;

  public GenericAlreadyExistsException(Exception e) {
    super(e);
  }

  public GenericAlreadyExistsException(final String message) {
    super(message);
  }
}
