package com.trc.service;

import com.trc.exception.GatewayException;
import com.trc.user.User;
import com.tscp.mvne.Account;

public interface EmailServiceModel {

  public void sendActivationEmail(User user, Account account) throws GatewayException;
}
