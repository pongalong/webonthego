package com.trc.exception.management;

import com.trc.exception.ManagementException;

public class RefundManagementException extends ManagementException {
  private static final long serialVersionUID = 1L;

  public RefundManagementException() {
    super();
  }

  public RefundManagementException(String message) {
    super(message);
  }

  public RefundManagementException(Throwable cause) {
    super(cause);
  }

  public RefundManagementException(String message, Throwable cause) {
    super(message, cause);
  }
}
