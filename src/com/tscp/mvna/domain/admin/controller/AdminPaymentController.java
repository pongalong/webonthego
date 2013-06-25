package com.tscp.mvna.domain.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.management.PaymentManagementException;
import com.trc.exception.service.PaymentFailureException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.tscp.mvna.web.controller.model.ResultModel;
import com.tscp.mvna.web.session.cache.CachedAttributeNotFound;
import com.tscp.mvne.PaymentUnitResponse;

@Controller
@RequestMapping("/admin/payment")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"ACCOUNT_DETAILS",
		"accountDetail",
		"topupAmount" })
public class AdminPaymentController {
	@Autowired
	private PaymentManager paymentManager;
	@Autowired
	private AccountManager accountManager;

	@RequestMapping(value = "/topup/queue", method = RequestMethod.GET)
	public ModelAndView queueTopup(
			@ModelAttribute("USER") User user) {

		ResultModel model = new ResultModel("admin/payment/topup/queue/prompt");
		return model.getSuccess();
	}

	@RequestMapping(value = "/topup/queue", method = RequestMethod.POST)
	public ModelAndView queueTopupPost(
			@ModelAttribute("USER") User user) {

		ResultModel model = new ResultModel("admin/payment/topup/queue/success", "admin/payment/topup/queue/prompt");

		try {
			paymentManager.queueTopup(user);
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getException();
		}
	}

	@RequestMapping(value = "/topup/force/{encodedDeviceId}", method = RequestMethod.GET)
	public ModelAndView forceTopup(
			@ModelAttribute("USER") User user, @ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @PathVariable("encodedDeviceId") String encodedDeviceId) {

		ResultModel model = new ResultModel("admin/payment/topup/force/prompt");

		try {
			AccountDetail accountDetail = getAccountDetailFromSession(accountDetails, encodedDeviceId);
			String topupAmount = paymentManager.getTopupAmount(user, accountDetail.getAccount());
			model.addAttribute("topupAmount", topupAmount);
			model.addAttribute("accountDetail", accountDetail);
			return model.getSuccess();
		} catch (PaymentManagementException e) {
			return model.getAccessException();
		}

	}

	@RequestMapping(value = "/topup/force/{encodedDeviceId}", method = RequestMethod.POST)
	public ModelAndView forceTopupPost(
			@ModelAttribute("USER") User user, @ModelAttribute("accountDetail") AccountDetail accountDetail, @ModelAttribute("topupAmount") String topupAmount) {

		ResultModel model = new ResultModel("admin/payment/topup/force/success", "admin/payment/topup/force/fail");

		try {
			PaymentUnitResponse response = paymentManager.makePayment(user, accountDetail.getAccount());
			model.addAttribute("paymentResponse", response);
			return model.getSuccess();
		} catch (PaymentFailureException e) {
			PaymentUnitResponse response = new PaymentUnitResponse();
			response.setAuthcode(e.getAuthCode());
			response.setConfcode(e.getResponse());
			response.setConfdescr(e.getMessage());
			model.addAttribute("paymentResponse", response);
			return model.getError();
		} catch (PaymentManagementException e) {
			model.addAttribute("paymentResponse", new PaymentUnitResponse());
			return model.getError();
		}
	}

	private AccountDetail getAccountDetailFromSession(
			List<AccountDetail> accountDetails, String encodedDeviceId) throws CachedAttributeNotFound {

		for (AccountDetail ad : accountDetails)
			if (ad.getEncodedDeviceId().equals(encodedDeviceId))
				return ad;

		throw new CachedAttributeNotFound("Could not find AccountDetail for Device");
	}
}