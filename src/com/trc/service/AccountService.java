package com.trc.service;

import java.util.List;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.service.AccountServiceException;
import com.trc.service.gateway.WebserviceGateway;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.user.User;
import com.trc.util.Formatter;
import com.tscp.mvne.Account;
import com.tscp.mvne.CustAcctMapDAO;
import com.tscp.mvne.CustInfo;
import com.tscp.mvne.CustTopUp;
import com.tscp.mvne.Customer;
import com.tscp.mvne.PaymentRecord;
import com.tscp.mvne.TSCPMVNA;
import com.tscp.mvne.UsageDetail;

@Service
public class AccountService implements AccountServiceModel {
	private TSCPMVNA port;

	@Autowired
	public void init(
			WebserviceGateway gateway) {
		this.port = gateway.getPort();
	}

	private Account makeAccount(
			User user) {
		Account account = new Account();
		account.setFirstname(user.getContactInfo().getFirstName());
		account.setLastname(user.getContactInfo().getLastName());
		account.setContactAddress1(user.getContactInfo().getAddress().getAddress1());
		account.setContactAddress2(user.getContactInfo().getAddress().getAddress2());
		account.setContactCity(user.getContactInfo().getAddress().getCity());
		account.setContactState(user.getContactInfo().getAddress().getState());
		account.setContactZip(user.getContactInfo().getAddress().getZip());
		account.setContactNumber(user.getContactInfo().getPhoneNumber());
		account.setContactEmail(user.getEmail());
		return account;
	}

	public Account getUnlinkedAccount(
			User user) throws AccountServiceException {
		try {
			return port.getUnlinkedAccount(user.getUserId());
		} catch (WebServiceException e) {
			throw new AccountServiceException(e);
		}
	}

	@Override
	public CustInfo getCustInfo(
			User user) throws AccountServiceException {
		try {
			return port.getCustInfo(WebserviceAdapter.toCustomer(user));
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public Account createShellAccount(
			User user) throws AccountServiceException {
		Account account = makeAccount(user);
		try {
			Account createdAccount = port.createBillingAccount(WebserviceAdapter.toCustomer(user), account);
			setTopUp(user, 10.00, createdAccount);
			return createdAccount;
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<CustAcctMapDAO> getAccountMap(
			User user) throws AccountServiceException {
		Customer customer = WebserviceAdapter.toCustomer(user);
		try {
			return port.getCustomerAccounts(customer.getId());
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<UsageDetail> getChargeHistory(
			User user,
			int accountNumber) throws AccountServiceException {
		try {
			return port.getCustomerChargeHistory(WebserviceAdapter.toCustomer(user), accountNumber, null);
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<PaymentRecord> getPaymentRecords(
			User user) throws AccountServiceException {
		try {
			return port.getPaymentHistory(WebserviceAdapter.toCustomer(user));
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void updateEmail(
			Account account) throws AccountServiceException {
		try {
			port.updateAccountEmailAddress(account);
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public Account getAccount(
			int accountNumber) throws AccountServiceException {
		try {
			return port.getAccountInfo(accountNumber);
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public CustTopUp getTopUp(
			User user,
			Account account) throws AccountServiceException {
		try {
			return port.getCustTopUpAmount(WebserviceAdapter.toCustomer(user), account);
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public CustTopUp setTopUp(
			User user,
			double amount,
			Account account) throws AccountServiceException {
		try {
			String stringAmount = Formatter.formatDollarAmount(amount);
			return port.setCustTopUpAmount(WebserviceAdapter.toCustomer(user), stringAmount, account);
		} catch (WebServiceException e) {
			throw new AccountServiceException(e.getMessage(), e.getCause());
		}
	}

}
