package com.trc.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.PaymentManagementException;
import com.trc.exception.service.PaymentFailureException;
import com.trc.exception.service.PaymentServiceException;
import com.trc.service.PaymentService;
import com.trc.user.User;
import com.trc.user.account.PaymentHistory;
import com.trc.user.account.PaymentMethodCollection;
import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.CustPmtMap;
import com.tscp.mvne.PaymentUnitResponse;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class PaymentManager implements PaymentManagerModel {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private CacheManager cacheManager;

	@Loggable(value = LogLevel.INFO)
	public void queueTopup(
			User user) throws PaymentManagementException {
		try {
			paymentService.queueTopup(user);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public String getTopupAmount(
			User user, Account account) throws PaymentManagementException {
		try {
			return paymentService.getTopupAmount(user, account);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.INFO)
	public PaymentUnitResponse makePayment(
			User user, Account account) throws PaymentManagementException, PaymentFailureException {
		try {
			int paymentId = paymentService.getDefaultPaymentMethodId(user);
			String topupAmount = paymentService.getTopupAmount(user, account);
			PaymentUnitResponse response = paymentService.makePayment(user, account, paymentId, topupAmount);
			PaymentHistory paymentHistory = accountManager.getPaymentHistory(user);
			cacheManager.cache(paymentHistory);
			// CacheManager.set(CacheKey.PAYMENT_HISTORY, new PaymentHistory(accountManager.getPaymentRecords(user), user));
			return response;
		} catch (PaymentFailureException e) {
			throw e;
		} catch (PaymentServiceException | AccountManagementException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.INFO)
	public PaymentUnitResponse makePayment(
			User user, Account account, int paymentId, String amount) throws PaymentFailureException, PaymentManagementException {
		try {
			PaymentUnitResponse response = paymentService.makePayment(user, account, paymentId, amount);
			cacheManager.cache(accountManager.getPaymentHistory(user));
			return response;
		} catch (AccountManagementException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public PaymentUnitResponse makePayment(
			User user, Account account, CreditCard creditCard, String amount) throws PaymentFailureException, PaymentManagementException {
		try {
			PaymentUnitResponse response = paymentService.makePayment(user, account, creditCard, amount);
			cacheManager.cache(accountManager.getPaymentHistory(user));
			return response;
		} catch (AccountManagementException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}

	}

	@Loggable(value = LogLevel.TRACE)
	public PaymentUnitResponse makeActivationPayment(
			User user, Account account, CreditCard creditCard) throws PaymentFailureException, PaymentManagementException {
		try {
			PaymentUnitResponse response = makePayment(user, account, creditCard.getPaymentid(), "10.00");
			cacheManager.cache(accountManager.getPaymentHistory(user));
			return response;
		} catch (AccountManagementException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}

	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public CreditCard getCreditCard(
			int paymentId) throws PaymentManagementException {
		CreditCard creditCard = getCreditCardFromCache(paymentId);
		if (creditCard != null) {
			return creditCard;
		} else {
			try {
				return paymentService.getCreditCard(paymentId);
			} catch (PaymentServiceException e) {
				throw new PaymentManagementException(e.getMessage(), e.getCause());
			}
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public CreditCard addCreditCard(
			User user, CreditCard creditCard) throws PaymentManagementException {
		try {
			// cacheManager.clear(CacheKey.PAYMENT_METHODS);
			cacheManager.clear(new PaymentMethodCollection());
			return paymentService.addCreditCard(user, creditCard);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	public List<CustPmtMap> removeCreditCard(
			User user, CreditCard creditCard) throws PaymentManagementException {
		try {
			return removeCreditCard(user, creditCard.getPaymentid());
		} catch (PaymentManagementException e) {
			throw e;
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public List<CustPmtMap> removeCreditCard(
			User user, int paymentId) throws PaymentManagementException {
		try {
			List<CustPmtMap> paymentMethods = paymentService.getPaymentMap(user);
			if (paymentMethods.size() > 1) {
				// cacheManager.clear(CacheKey.PAYMENT_METHODS);
				cacheManager.clear(new PaymentMethodCollection());
				return paymentService.removeCreditCard(user, paymentId);
			} else {
				return paymentMethods;
			}
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public List<CustPmtMap> updateCreditCard(
			User user, CreditCard creditCard) throws PaymentManagementException {
		try {
			// cacheManager.clear(CacheKey.PAYMENT_METHODS);
			cacheManager.clear(new PaymentMethodCollection());
			return paymentService.updateCreditCard(user, creditCard);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public List<CustPmtMap> getPaymentMap(
			User user) throws PaymentManagementException {
		try {
			return paymentService.getPaymentMap(user);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public CustPmtMap getPaymentMap(
			User user, int paymentId) throws PaymentManagementException {
		try {
			return paymentService.getPaymentMap(user, paymentId);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	@Override
	@Loggable(value = LogLevel.TRACE)
	public List<CustPmtMap> updatePaymentMap(
			CustPmtMap custPmtMap) throws PaymentManagementException {
		try {
			return paymentService.updatePaymentMap(custPmtMap);
		} catch (PaymentServiceException e) {
			throw new PaymentManagementException(e.getMessage(), e.getCause());
		}
	}

	public boolean paymentSuccess(
			PaymentUnitResponse response) {
		return response.getConfcode().equals("0");
	}

	public boolean errorExpirationDate(
			String errorMessage) {
		return errorMessage.contains("");
	}

	@Loggable(value = LogLevel.TRACE)
	public PaymentMethodCollection getCreditCards(
			User user) throws PaymentManagementException {
		// List<CreditCard> creditCardList = getCreditCardListFromCache();
		PaymentMethodCollection creditCardCollection = (PaymentMethodCollection) cacheManager.fetch(new PaymentMethodCollection());
		if (creditCardCollection != null) {
			return creditCardCollection;
		} else {
			try {
				creditCardCollection = new PaymentMethodCollection();
				List<CustPmtMap> paymentMap = getPaymentMap(user);
				for (CustPmtMap paymentMethod : paymentMap) {
					creditCardCollection.add(getCreditCard(paymentMethod.getPaymentid()));
				}
				// cacheManager.set(CacheKey.PAYMENT_METHODS, creditCardList);
				cacheManager.cache(creditCardCollection);
				return creditCardCollection;
			} catch (PaymentManagementException e) {
				throw e;
			}
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public CreditCard getDefaultCreditCard(
			User user) throws PaymentManagementException {
		try {
			List<CustPmtMap> custPmtMap = getPaymentMap(user);
			if (custPmtMap.size() > 0) {
				return getCreditCard(custPmtMap.get(0).getPaymentid());
			} else {
				return null;
			}
		} catch (PaymentManagementException e) {
			throw e;
		}
	}

	private CreditCard getCreditCardFromCache(
			int paymentId) {
		PaymentMethodCollection creditCards = (PaymentMethodCollection) cacheManager.fetch(new PaymentMethodCollection());
		if (creditCards != null)
			for (CreditCard cc : creditCards)
				if (cc.getPaymentid() == paymentId)
					return cc;
		return null;
	}

}