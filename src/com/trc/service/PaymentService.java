package com.trc.service;

import java.util.List;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.service.PaymentFailureException;
import com.trc.exception.service.PaymentServiceException;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.service.gateway.WebserviceGateway;
import com.trc.user.User;
import com.trc.web.session.SessionManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.CustPmtMap;
import com.tscp.mvne.CustTopUp;
import com.tscp.mvne.PaymentUnitResponse;
import com.tscp.mvne.TSCPMVNA;

@Service
public class PaymentService implements PaymentServiceModel {
	private TSCPMVNA port;

	@Autowired
	public void init(
			WebserviceGateway gateway) {
		this.port = gateway.getPort();
	}

	public void queueTopup(
			User user) throws PaymentServiceException {
		try {
			port.paymentUpdatedRoutine(WebserviceAdapter.toCustomer(user));
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public CreditCard getCreditCard(
			int paymentId) throws PaymentServiceException {
		try {
			CreditCard creditCard = port.getCreditCardDetail(paymentId);
			if (creditCard.getAddress2() != null && creditCard.getAddress2().equals("{}")) {
				creditCard.setAddress2(null);
			}
			return creditCard;
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public CreditCard addCreditCard(
			User user, CreditCard creditCard) throws PaymentServiceException {
		try {
			if (user == null || creditCard.getIsDefault() == null) {
				creditCard.setIsDefault("N");
			}
			return port.addCreditCard(WebserviceAdapter.toCustomer(user), creditCard);
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<CustPmtMap> removeCreditCard(
			User user, int paymentId) throws PaymentServiceException {
		try {
			List<CustPmtMap> paymentMapList = port.deleteCreditCardPaymentMethod(WebserviceAdapter.toCustomer(user), paymentId);
			if (!paymentMapList.isEmpty()) {
				boolean updateDefault = true;
				CustPmtMap newDefault = paymentMapList.get(0);
				for (CustPmtMap paymentMap : paymentMapList) {
					if (paymentMap.getIsDefault().equals("Y")) {
						updateDefault = false;
					}
				}
				if (updateDefault) {
					newDefault.setIsDefault("Y");
					return updatePaymentMap(newDefault);
				}
			}
			return paymentMapList;
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<CustPmtMap> updateCreditCard(
			User user, CreditCard creditCard) throws PaymentServiceException {
		try {
			if (creditCard.getAddress2() == null || creditCard.getAddress2().isEmpty()) {
				creditCard.setAddress2("{}");
			}
			if (creditCard.getCreditCardNumber().toLowerCase().contains("x")) {
				creditCard.setCreditCardNumber(null);
			}
			List<CustPmtMap> paymentMapList = port.updateCreditCardPaymentMethod(WebserviceAdapter.toCustomer(user), creditCard);
			CustPmtMap paymentMap = getPaymentMap(paymentMapList, creditCard.getPaymentid());
			paymentMap.setPaymentalias(creditCard.getAlias());
			if (creditCard.getIsDefault() == null) {
				paymentMap.setIsDefault("N");
			} else {
				paymentMap.setIsDefault("Y");
			}
			return updatePaymentMap(paymentMap);
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	private CustPmtMap getPaymentMap(
			List<CustPmtMap> paymentMapList, int paymentId) {
		for (CustPmtMap custPmtMap : paymentMapList) {
			if (custPmtMap.getPaymentid() == paymentId)
				return custPmtMap;
		}
		return null;
	}

	@Override
	public List<CustPmtMap> getPaymentMap(
			User user) throws PaymentServiceException {
		try {
			return port.getCustPaymentList(user.getUserId(), 0);
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public CustPmtMap getPaymentMap(
			User user, int paymentId) throws PaymentServiceException {
		try {
			List<CustPmtMap> result = port.getCustPaymentList(user.getUserId(), paymentId);
			return result.size() == 1 ? result.get(0) : null;
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public List<CustPmtMap> updatePaymentMap(
			CustPmtMap custPmtMap) throws PaymentServiceException {
		try {
			return port.updateCustPaymentMap(custPmtMap);
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public PaymentUnitResponse makePayment(
			User user, Account account, CreditCard creditCard, String amount) throws PaymentFailureException {
		try {
			return port.submitPaymentByCreditCard(SessionManager.getCurrentSession().getId(), account, creditCard, amount);
		} catch (WebServiceException e) {
			throw new PaymentFailureException(e);
		}
	}

	@Override
	public PaymentUnitResponse makePayment(
			User user, Account account, int paymentId, String amount) throws PaymentFailureException {
		try {
			return port.submitPaymentByPaymentId(SessionManager.getCurrentSession().getId(), WebserviceAdapter.toCustomer(user), paymentId, account, amount);
		} catch (WebServiceException e) {
			throw new PaymentFailureException(e);
		}
	}

	public String getTopupAmount(
			User user, Account account) throws PaymentServiceException {
		try {
			CustTopUp topup = port.getCustTopUpAmount(WebserviceAdapter.toCustomer(user), account);
			return topup.getTopupAmount();
		} catch (WebServiceException e) {
			throw new PaymentServiceException(e.getMessage(), e.getCause());
		}
	}

	public int getDefaultPaymentMethodId(
			User user) throws PaymentServiceException {

		List<CustPmtMap> paymentMap = getPaymentMap(user);

		if (paymentMap != null) {
			if (paymentMap.size() == 1)
				return paymentMap.get(0).getPaymentid();
			for (CustPmtMap cpm : paymentMap)
				if (cpm.getIsDefault().equalsIgnoreCase("Y"))
					return cpm.getPaymentid();
		}

		return 0;
	}

	public CreditCard getDefaultPaymentMethod(
			User user) throws PaymentServiceException {
		int paymentId = getDefaultPaymentMethodId(user);
		if (paymentId > 0)
			return getCreditCard(paymentId);
		else
			return null;
	}
}
