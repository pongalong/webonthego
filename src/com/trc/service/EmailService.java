package com.trc.service;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.GatewayException;
import com.trc.service.gateway.TSCPMVNEGateway;
import com.trc.service.gateway.TSCPMVNAUtil;
import com.trc.user.User;
import com.tscp.mvne.Account;
import com.tscp.mvne.TSCPMVNA;

@Service
public class EmailService implements EmailServiceModel {
  private TSCPMVNA port;

  @Autowired
  public void init(TSCPMVNEGateway gateway) {
    this.port = gateway.getPort();
  }

  @Override
  public void sendActivationEmail(User user, Account account) throws GatewayException {
    try {
      port.sendWelcomeNotification(TSCPMVNAUtil.toCustomer(user), account);
    } catch (WebServiceException e) {
      throw new GatewayException(e.getMessage(), e.getCause());
    }
  }

}
