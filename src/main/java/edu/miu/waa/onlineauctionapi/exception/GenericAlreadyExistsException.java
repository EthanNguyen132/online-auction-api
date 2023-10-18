package edu.miu.waa.onlineauctionapi.exception;

import java.io.Serial;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot Ed 2
 */
public class GenericAlreadyExistsException extends RuntimeException {

  @Serial private static final long serialVersionUID = 3946919557745116819L;

  public GenericAlreadyExistsException(Exception e) {
    super(e);
  }

  public GenericAlreadyExistsException(final String message) {
    super(message);
  }
}
