package com.tscp.mvna.domain.payment.coupon;

public class CouponResponse {
  private boolean valid;
  private String description;

  public boolean isValid() {
    return valid;
  }

  public String getDescription() {
    return description;
  }

  public CouponResponse(boolean valid, String description) {
    this.valid = valid;
    this.description = description;
  }
}