package com.trc.service;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.GatewayException;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.service.gateway.WebserviceGateway;
import com.trc.user.User;
import com.tscp.mvne.Account;
import com.tscp.mvne.TSCPMVNA;

@Service
public class EmailService implements EmailServiceModel {
	private TSCPMVNA port;

	@Autowired
	public void init(WebserviceGateway gateway) {
		this.port = gateway.getPort();
	}

	@Override
	public void sendActivationEmail(User user, Account account) throws GatewayException {
		try {
			port.sendActivationSuccessNotice(WebserviceAdapter.toCustomer(user), account);
		} catch (WebServiceException e) {
			throw new GatewayException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void sendRegistrationEmail(User user) throws GatewayException {
		try {
			port.sendRegistrationSuccessNotice(user.getUserId(), user.getEmail(), user.getUsername());
		} catch (WebServiceException e) {
			throw new GatewayException(e.getMessage(), e.getCause());
		}
	}

}
