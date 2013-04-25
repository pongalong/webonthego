package com.trc.domain.refund;

public enum RefundCode {
  UNSELECTED(0, "Select one..."), 
  UNNECESSARY_TOPUP(1, "Unneccessary top-up"), 
  DOUBLE_TOPUP(2, "Double top-up"),
  UNNECESSARY_WEB_PAYMENT(3, "Unnecessory Web Payment"), 
  INTERNAL_REFUND_REQUEST(4, "Internal Refund Request"),
  UNNECESSARY_MRC(5, "Unnecessory MRC"); 
   
  private int value;
  private String description;

  private RefundCode(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public int getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

}
