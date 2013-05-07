package com.trc.user.activation.simple;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.trc.coupon.Coupon;
import com.trc.coupon.CouponRequest;
import com.trc.exception.EmailException;
import com.trc.exception.GatewayException;
import com.trc.exception.WebFlowException;
import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.AddressManagementException;
import com.trc.exception.management.CouponDoesNotExistException;
import com.trc.exception.management.CouponManagementException;
import com.trc.exception.management.DeviceActivationException;
import com.trc.exception.management.DeviceDisconnectException;
import com.trc.exception.management.DeviceManagementException;
import com.trc.exception.management.DeviceReservationException;
import com.trc.exception.management.DeviceServiceCreationException;
import com.trc.exception.management.PaymentManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.CouponManager;
import com.trc.manager.DeviceManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.UserManager;
import com.trc.service.EmailService;
import com.trc.service.email.VelocityEmailService;
import com.trc.service.gateway.WebserviceAdapter;
import com.trc.user.User;
import com.trc.user.contact.Address;
import com.trc.user.contact.ContactInfo;
import com.trc.web.flow.util.WebFlowUtil;
import com.trc.web.session.cache.CacheManager;
import com.tscp.mvne.Account;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.Device;
import com.tscp.mvne.NetworkInfo;
import com.tscp.mvne.PaymentUnitResponse;
import com.tscp.mvne.ServiceInstance;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class SimpleActivationManager {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private DeviceManager deviceManager;
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private CouponManager couponManager;
	@Autowired
	private EmailService emailService;
	@Autowired
	private VelocityEmailService velocityEmailService;
	@Autowired
	private CacheManager cacheManager;

	static final Logger logger = LoggerFactory.getLogger(SimpleActivationManager.class);

	/* ***********************************************************************
	 * Decision and Fetch Methods
	 */

	public boolean isListEmpty(
			List<Object> list) {
		return list == null || list.isEmpty();
	}

	public boolean hasCoupon(
			Coupon coupon) {
		boolean result = coupon != null && !coupon.isEmpty();
		logger.trace("coupon entered... {}", result);
		return result;
	}

	public List<CreditCard> fetchPaymentMethods(
			User user) throws PaymentManagementException {
		List<CreditCard> creditCards = paymentManager.getCreditCards(user);
		logger.trace("{} Fetching existing payment methods... {} found", user.getEmail(), creditCards.size());
		return creditCards;
	}

	public Account getUnlinkedAccount(
			User user) {
		try {
			Account account = accountManager.getUnlinkedAccount(user);
			if (account != null)
				logger.info("{} Found unlinked account {}", user.getEmail(), account.getAccountNo());
			return account;
		} catch (AccountManagementException e) {
			return null;
		}
	}

	/* ***********************************************************************
	 * Account Creation Methods
	 */

	@Loggable(value = LogLevel.TRACE)
	public Account createShellAccount(
			SimpleActivation sa) throws AccountManagementException, AddressManagementException {
		logger.trace("{} Creating shell account", sa.getUser().getEmail());

		CreditCard creditCard = sa.getCreditCardPayment().getCreditCard();

		String firstName;
		String lastName;
		int indexOfSpace = creditCard.getNameOnCreditCard().lastIndexOf(" ");
		if (indexOfSpace > -1) {
			firstName = creditCard.getNameOnCreditCard().substring(0, creditCard.getNameOnCreditCard().lastIndexOf(" "));
			lastName = creditCard.getNameOnCreditCard().substring(creditCard.getNameOnCreditCard().lastIndexOf(" ") + 1);
		} else {
			firstName = creditCard.getNameOnCreditCard();
			lastName = "";
		}

		Address address = new Address(creditCard);
		address.setDefault(true);

		ContactInfo contactInfo = sa.getUser().getContactInfo();
		contactInfo.setFirstName(firstName);
		contactInfo.setLastName(lastName);
		contactInfo.setEmail(sa.getUser().getEmail());
		contactInfo.setPhoneNumber(sa.getCreditCardPayment().getPhoneNumber());
		contactInfo.setAddress(address);

		Account account = accountManager.createShellAccount(sa.getUser());
		logger.info("{} Shell account created with account number {}", sa.getUser().getEmail(), account.getAccountNo());
		return account;
	}

	/* ***********************************************************************
	 * Device Methods
	 */

	public String getSuggestedName(
			String firstName) {
		return firstName + "'s Device";
	}

	public Device addDevice(
			SimpleActivation sa) throws DeviceManagementException {
		logger.trace("{} Adding device with ESN {} to user {}", sa.getUser().getEmail(), sa.getDevice().getValue());
		sa.setDevice(deviceManager.addDeviceInfo(sa.getDevice(), sa.getAccount(), sa.getUser()));
		logger.info("{} Added device with ESN {} with ID {}", new Object[] {
				sa.getUser().getEmail(),
				sa.getDevice().getValue(),
				sa.getDevice().getId() });
		return sa.getDevice();
	}

	public List<Device> removeDevice(
			SimpleActivation sa) throws DeviceManagementException {
		logger.trace("{} Removing device with ESN {} and ID {}", new Object[] {
				sa.getUser().getEmail(),
				sa.getDevice().getValue(),
				sa.getDevice().getId() });
		List<Device> devices = deviceManager.removeDeviceInfo(sa.getDevice(), sa.getAccount(), sa.getUser());
		logger.info("{} Removed device with ESN {}", sa.getUser().getEmail(), sa.getDevice().getValue());
		return devices;
	}

	public void releaseMdn(
			SimpleActivation sa) throws DeviceDisconnectException {
		logger.trace("{} Releasing MDN {} on device {} for failed activation", new Object[] {
				sa.getUser().getEmail(),
				sa.getNetworkInfo().getMdn(),
				sa.getDevice().getValue() });
		releaseMdn(sa, 3, 1000l);
	}

	protected void releaseMdn(
			SimpleActivation sa, int numRetry, long delay) throws DeviceDisconnectException {
		logger.trace("{} Releasing MDN {} on device {}, {} attempts left", new Object[] {
				sa.getUser().getEmail(),
				sa.getNetworkInfo().getMdn(),
				sa.getDevice().getValue(),
				numRetry });
		if (numRetry > 0) {
			try {
				if (delay > 0)
					Thread.sleep(delay);
				deviceManager.disconnectFromNetwork(sa.getNetworkInfo());
				logger.info("{} Released MDN {} on device {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getNetworkInfo().getMdn(),
						sa.getDevice().getValue() });
				sa.setNetworkInfo(null);
			} catch (InterruptedException e) {
				throw new DeviceDisconnectException("Thread sleep interrupted. Disconnect never reached.");
			} catch (DeviceDisconnectException e) {
				releaseMdn(sa, --numRetry, delay);
			}
		} else {
			throw new DeviceDisconnectException("Max number of tries reached. MDN " + sa.getNetworkInfo().getMdn() + " not released.");
		}
	}

	public NetworkInfo reserveMdn(
			SimpleActivation sa) throws DeviceReservationException {
		logger.trace("{} Reserving MDN for device {}", sa.getUser().getEmail(), sa.getDevice().getValue());
		return reserveMdn(sa, 3, 1000l);
	}

	protected NetworkInfo reserveMdn(
			SimpleActivation sa, int numRetry, long delay) throws DeviceReservationException {
		logger.trace("{} Reserving MDN for device {}, {} attempts left", new Object[] {
				sa.getUser().getEmail(),
				sa.getDevice().getValue(),
				numRetry });
		if (numRetry > 0) {
			try {
				if (delay > 0)
					Thread.sleep(delay);
				sa.setNetworkInfo(deviceManager.reserveMdn());
				logger.info("{} Reserved MDN {} for device {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getNetworkInfo().getMdn(),
						sa.getDevice().getValue() });
				return sa.getNetworkInfo();
			} catch (InterruptedException e) {
				throw new DeviceReservationException("Thread sleep interrupted. MDN not reserved.");
			} catch (DeviceReservationException e) {
				return reserveMdn(sa, --numRetry, delay);
			}
		} else {
			throw new DeviceReservationException("Max number of tries reached. MDN not reserved for account " + sa.getAccount().getAccountNo());
		}
	}

	public NetworkInfo activateService(
			SimpleActivation sa) throws DeviceActivationException {
		if (DeviceManager.isDec(sa.getDevice().getValue())) {
			sa.getNetworkInfo().setEsnmeiddec(sa.getDevice().getValue());
		} else if (DeviceManager.isHex(sa.getDevice().getValue())) {
			sa.getNetworkInfo().setEsnmeidhex(sa.getDevice().getValue());
		} else {
			throw new DeviceActivationException("Invalid ESN. Must be either hex or decimal");
		}
		logger.trace("{} Activating service for device {} on MDN {}", new Object[] {
				sa.getUser().getEmail(),
				sa.getDevice().getValue(),
				sa.getNetworkInfo().getMdn() });
		return activateService(sa, 3, 1000l);
	}

	protected NetworkInfo activateService(
			SimpleActivation sa, int numRetry, long delay) throws DeviceActivationException {
		logger.trace("{} Activating service for device {} on MDN {}, {} attempts left", new Object[] {
				sa.getUser().getEmail(),
				sa.getDevice().getValue(),
				sa.getNetworkInfo().getMdn(),
				numRetry });
		if (numRetry > 0) {
			try {
				if (delay > 0)
					Thread.sleep(delay);
				sa.setNetworkInfo(deviceManager.activateService(sa.getNetworkInfo(), sa.getUser()));
				logger.info("{} Activated service for deivce {} on MDN {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getDevice().getValue(),
						sa.getNetworkInfo().getMdn() });
				return sa.getNetworkInfo();
			} catch (InterruptedException e) {
				throw new DeviceActivationException("Thread sleep interrupted. Activation never reached");
			} catch (DeviceActivationException e) {
				return activateService(sa, --numRetry, delay);
			}
		} else {
			throw new DeviceActivationException("Max number of tries reached. Activation not completed for account " + sa.getAccount().getAccountNo() + " on ESN " + sa.getDevice().getValue() + " with MDN " + sa.getNetworkInfo().getMdn());
		}
	}

	public Account createService(
			SimpleActivation sa) throws DeviceServiceCreationException {
		sa.setAccount(createService(sa, 3, 1000l));
		logger.trace("{} Creating service instance for account {} and MDN {}", new Object[] {
				sa.getUser().getEmail(),
				sa.getAccount().getAccountNo(),
				sa.getNetworkInfo().getMdn() });
		return sa.getAccount();
	}

	protected Account createService(
			SimpleActivation sa, int numRetry, long delay) throws DeviceServiceCreationException {
		logger.trace("{} Creating service instance for account {} and MDN {}, {} attempts left", new Object[] {
				sa.getUser().getEmail(),
				sa.getAccount().getAccountNo(),
				sa.getNetworkInfo().getMdn(),
				numRetry });
		if (numRetry > 0) {
			try {
				if (delay > 0)
					Thread.sleep(delay);
				sa.setAccount(deviceManager.createServiceInstance(sa.getAccount(), sa.getNetworkInfo()));
				logger.info("{} Created service instance for account {} and MDN {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getAccount().getAccountNo(),
						sa.getNetworkInfo().getMdn() });
				return sa.getAccount();
			} catch (InterruptedException e) {
				throw new DeviceServiceCreationException("Thread sleep interrupted. Service not created for account " + sa.getAccount().getAccountNo() + " with MDN " + sa.getNetworkInfo().getMdn());
			} catch (DeviceServiceCreationException e) {
				return createService(sa, --numRetry, delay);
			}
		} else {
			throw new DeviceServiceCreationException("Max number of tries reached. Service not created for account " + sa.getAccount().getAccountNo() + " with MDN " + sa.getNetworkInfo().getMdn());
		}
	}

	public NetworkInfo reserveAndActivate(
			SimpleActivation sa) throws DeviceReservationException, DeviceActivationException, DeviceDisconnectException, DeviceManagementException {
		try {
			reserveMdn(sa);
			return activateService(sa);
		} catch (DeviceReservationException e) {
			throw e;
		} catch (DeviceActivationException e) {
			throw e;
		}
	}

	/* ***********************************************************************
	 * Coupon Methods
	 */

	public Coupon getCouponByCode(
			Coupon coupon) throws CouponManagementException, CouponDoesNotExistException {
		String code = coupon.getCouponCode();
		logger.trace("fetching coupon {}", code);
		Coupon fetchedCoupon = couponManager.getCouponByCode(code);
		if (fetchedCoupon == null) {
			logger.error("coupon {} does not exist", code);
			throw new CouponDoesNotExistException("Coupon " + code + " does not exist");
		} else {
			return fetchedCoupon;
		}
	}

	public int applyCoupon(
			SimpleActivation sa) throws CouponManagementException {
		Coupon coupon = sa.getCreditCardPayment().getCoupon();
		if (coupon != null && !coupon.isEmpty()) {
			if (coupon.getCouponDetail().getContract().getContractType() == -1) {
				logger.trace("{} Applying coupon {} on account {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getCreditCardPayment().getCoupon().getCouponCode(),
						sa.getAccount().getAccountNo() });
				ServiceInstance serviceInstance = new ServiceInstance();
				serviceInstance.setExternalId("");

				CouponRequest couponRequest = new CouponRequest();
				couponRequest.setCoupon(sa.getCreditCardPayment().getCoupon());
				couponRequest.setUser(sa.getUser());
				couponRequest.setAccount(sa.getAccount());

				int result = couponManager.applyCoupon(couponRequest);
				logger.trace("{} Applied coupon {} on account {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getCreditCardPayment().getCoupon().getCouponCode(),
						sa.getAccount().getAccountNo() });
				return result;
			} else {
				logger.trace("no coupon to apply");
			}
		}
		logger.trace("no coupon to apply");
		return 0;
	}

	public int applyContract(
			SimpleActivation sa) throws CouponManagementException {
		Coupon coupon = sa.getCreditCardPayment().getCoupon();
		if (coupon != null && !coupon.isEmpty()) {
			if (coupon.getCouponDetail().getContract().getContractType() != -1) {
				logger.trace("{} Applying contract {} on account {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getCreditCardPayment().getCoupon().getCouponCode(),
						sa.getAccount().getAccountNo() });
				ServiceInstance serviceInstance = new ServiceInstance();
				serviceInstance.setExternalId(sa.getNetworkInfo().getMdn());

				CouponRequest couponRequest = new CouponRequest();
				couponRequest.setCoupon(sa.getCreditCardPayment().getCoupon());
				couponRequest.setUser(sa.getUser());
				couponRequest.setAccount(sa.getAccount());

				int result = couponManager.applyCoupon(couponRequest);
				logger.trace("{} Applied contract {} on account {}", new Object[] {
						sa.getUser().getEmail(),
						sa.getCreditCardPayment().getCoupon().getCouponCode(),
						sa.getAccount().getAccountNo() });
				return result;
			} else {
				logger.trace("no contract to apply");
			}
		}
		logger.trace("no contract to apply");
		return 0;
	}

	/* ***********************************************************************
	 * Payment Methods
	 */

	public void saveOrUpdatePaymentMethod(
			User user, CreditCard creditCard) throws PaymentManagementException {
		if (creditCard.getPaymentid() == 0)
			savePaymentMethod(user, creditCard);
		else
			updatePaymentMethod(user, creditCard);
	}

	public void savePaymentMethod(
			User user, CreditCard creditCard) throws PaymentManagementException {
		int ccLength = creditCard.getCreditCardNumber().length();
		String shortCc = creditCard.getCreditCardNumber().substring(ccLength - 4, ccLength);
		logger.trace("{} Adding payment method {}", user.getEmail(), shortCc);
		CreditCard createdCreditCard = paymentManager.addCreditCard(user, creditCard);
		logger.info("{} Added payment method {} with ID {}", new Object[] {
				user.getEmail(),
				shortCc,
				createdCreditCard.getPaymentid() });
		WebserviceAdapter.copyCreditCard(creditCard, createdCreditCard);
	}

	public void updatePaymentMethod(
			User user, CreditCard creditCard) throws PaymentManagementException {
		logger.trace("{} Updating payment method {}", user.getEmail(), creditCard.getPaymentid());
		paymentManager.updateCreditCard(user, creditCard);
	}

	public CreditCard getPaymentMethod(
			int paymentId) throws PaymentManagementException {
		CreditCard cc = paymentManager.getCreditCard(paymentId);
		logger.trace("Fetching payment method with ID {}", paymentId);
		return cc;
	}

	public CreditCard getDefaultPaymentMethod(
			User user) throws PaymentManagementException {
		logger.trace("{} Fetching default payment method", user.getEmail());
		return paymentManager.getDefaultCreditCard(user);
	}

	public PaymentUnitResponse makeActivationPayment(
			SimpleActivation sa) throws PaymentManagementException {
		User user = sa.getUser();
		Account account = sa.getAccount();
		CreditCard cc = sa.getCreditCardPayment().getCreditCard();

		logger.trace("{} Making activation payment on account {} with payment ID {}", new Object[] {
				user.getEmail(),
				account.getAccountNo(),
				cc.getPaymentid() });

		double amount = 10.00;

		// This calculates the remaining initial activation fee minus any amount they may have received by coupon code
		// Coupon coupon = sa.getCreditCardPayment().getCoupon();
		// if (coupon != null && !coupon.isEmpty())
		// if (coupon.getCouponDetail().getContract().getContractType() == -1) {
		// amount = amount - coupon.getCouponDetail().getAmount();
		// logger.info("{} Coupon applied, remaining activaiton fee of {}", user.getEmail(), amount);
		// }

		if (amount > 0.0) {
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			String moneyString = formatter.format(amount);
			logger.trace("{} Sending payment of {}", user.getEmail(), moneyString.substring(1));
			PaymentUnitResponse response = paymentManager.makePayment(user, account, cc.getPaymentid(), moneyString.substring(1));
			logger.trace("{} Made payment of {} on account {} with payment ID {}", new Object[] {
					user.getEmail(),
					moneyString,
					account.getAccountNo(),
					cc.getPaymentid() });
			return response;
		} else {
			return new PaymentUnitResponse();
		}

	}

	/* ***********************************************************************
	 * Helper Methods
	 */

	public void addFlowError(
			String message) {
		WebFlowUtil.addError(message);
	}

	public void addFlowError(
			String code, String message) {
		WebFlowUtil.addError(code, message);
	}

	public void addFlowError(
			String source, String code, String message) {
		WebFlowUtil.addError(source, code, message);
	}

	public void trace(
			String pattern, Object obj) {
		logger.trace(pattern, obj);
	}

	public void printStatus(
			SimpleActivation simpleActivation) {
		if (simpleActivation != null) {
			logger.debug("activation state:");
			if (simpleActivation.getUser() != null)
				logger.debug("  user: {} {}", simpleActivation.getUser().getUserId(), simpleActivation.getUser().getEmail());
			if (simpleActivation.getCreditCardPayment() != null)
				logger.debug("  cc: {} {}", simpleActivation.getCreditCardPayment().getCreditCard().getPaymentid(), simpleActivation.getCreditCardPayment().getCreditCard().getCreditCardNumber());
			if (simpleActivation.getDevice() != null)
				logger.debug("  device: {} {}", simpleActivation.getDevice().getId(), simpleActivation.getDevice().getValue());
			if (simpleActivation.getAccount() != null)
				logger.debug("  account: {}", simpleActivation.getAccount().getAccountNo());
		}
	}

	public void sendActivationNotice(
			SimpleActivation sa) {
		logger.trace("sending activation notice to user");
		try {
			emailService.sendActivationEmail(sa.getUser(), sa.getAccount());
		} catch (GatewayException e) {
			logger.error("Error sending activation email for user " + sa.getUser().getEmail() + " account " + sa.getAccount().getAccountNo());
		}
	}

	public void sendDisconnectExceptionNotice(
			SimpleActivation sa) {
		try {
			SimpleMailMessage myMessage = new SimpleMailMessage();
			myMessage.setTo("wotg_alerts@telscape.net");
			myMessage.setFrom("system-activations@webonthego.com");
			myMessage.setSubject("Exception while disconnecting MDN " + sa.getNetworkInfo().getMdn());
			Map<Object, Object> mailModel = new HashMap<Object, Object>();
			mailModel.put("dateTime", new Date());
			mailModel.put("email", sa.getUser().getEmail());
			mailModel.put("userId", sa.getUser().getUserId());
			mailModel.put("accountNo", sa.getAccount().getAccountNo());
			mailModel.put("mdn", sa.getNetworkInfo().getMdn());
			mailModel.put("esn", sa.getNetworkInfo().getEsnmeiddec());
			velocityEmailService.send("error_test_activation", myMessage, mailModel);
		} catch (EmailException e) {
			// do nothing
		}
	}

	public void notifyReleaseMdnException(
			SimpleActivation sa) {
		try {
			SimpleMailMessage myMessage = new SimpleMailMessage();
			myMessage.setTo("wotg_alerts@telscape.net");
			myMessage.setFrom("system-activations@webonthego.com");
			myMessage.setSubject("Exception while releasing a reserved MDN on a failed activation " + sa.getNetworkInfo().getMdn());
			Map<Object, Object> mailModel = new HashMap<Object, Object>();
			mailModel.put("dateTime", new Date());
			mailModel.put("email", sa.getUser().getEmail());
			mailModel.put("userId", sa.getUser().getUserId());
			mailModel.put("accountNo", sa.getAccount().getAccountNo());
			mailModel.put("mdn", sa.getNetworkInfo().getMdn());
			mailModel.put("esn", sa.getNetworkInfo().getEsnmeiddec());
			velocityEmailService.send("error_test_activation", myMessage, mailModel);
		} catch (EmailException e) {
			// do nothing
		}
	}

	public void sendServiceErrorNotice(
			SimpleActivation sa) throws WebFlowException {
		try {
			SimpleMailMessage myMessage = new SimpleMailMessage();
			myMessage.setTo("truconnect_alerts@telscape.net");
			myMessage.setFrom("system-activations@truconnect.com");
			myMessage.setSubject("Exception while creating service for Account " + sa.getAccount().getAccountNo());
			Map<Object, Object> mailModel = new HashMap<Object, Object>();
			mailModel.put("dateTime", new Date());
			mailModel.put("email", sa.getUser().getEmail());
			mailModel.put("userId", sa.getUser().getUserId());
			mailModel.put("accountNo", sa.getAccount().getAccountNo());
			mailModel.put("mdn", sa.getNetworkInfo().getMdn());
			mailModel.put("esn", sa.getNetworkInfo().getEsnmeiddec());
			velocityEmailService.send("error_create_service", myMessage, mailModel);
		} catch (EmailException e) {
			// do nothing
		}
	}

	public void refreshCache(
			SimpleActivation sa) {
		cacheManager.refreshCache(sa.getUser());
	}

}
