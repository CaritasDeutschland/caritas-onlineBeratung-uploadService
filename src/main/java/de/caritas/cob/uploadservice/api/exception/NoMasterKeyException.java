package de.caritas.cob.uploadservice.api.exception;

import java.io.Serial;

public class NoMasterKeyException extends Exception {

  @Serial private static final long serialVersionUID = 362702101121444833L;

  /**
   * Exception, when no master-key is set
   *
   * @param message
   */
  public NoMasterKeyException(String message) {
    super(message);
  }
}
