package com.tscp.mvna.domain.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.coupon.Coupon;
import com.trc.coupon.ajax.CouponResponse;
import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.CouponManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.CouponManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.web.model.ResultModel;
import com.trc.web.session.cache.CacheManager;
import com.trc.web.validation.CouponValidator;
import com.tscp.mvne.Account;
import com.tscp.mvne.ServiceInstance;
import com.tscp.util.logger.DevLogger;

@Controller
@RequestMapping("/coupons")
@SessionAttributes({ "USER", "CONTROLLING_USER", "ACCOUNT_DETAILS", "coupon" })
public class CouponController {
	@Autowired
	private CouponManager couponManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private CouponValidator couponValidator;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public @ResponseBody
	CouponResponse validateCoupon(
			@RequestParam String couponCode) {
		try {
			Coupon coupon = couponManager.getCouponByCode(couponCode);
			if (coupon != null) {
				if (coupon.getCouponDetail().getContract().getContractType() > 0) {
					return new CouponResponse(true, coupon.getCouponDetail().getContract().getDescription() + " for " + coupon.getCouponDetail().getDuration()
							+ " months");
				} else {
					return new CouponResponse(true, "Credit value of $" + coupon.getCouponDetail().getAmount());
				}
			} else {
				return new CouponResponse(false, "Not a valid coupon");
			}
		} catch (CouponManagementException e) {
			return new CouponResponse(false, "Internal error, try again");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showRedeemCoupon() {

		ResultModel model = new ResultModel("account/coupon/redeem/prompt");

		model.addAttribute("coupon", new Coupon());

		return model.getSuccess();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView postRedeemCoupon(
			HttpServletRequest request,
			@ModelAttribute("USER") User user,
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails,
			@ModelAttribute("coupon") Coupon coupon,
			BindingResult result) {

		ResultModel model = new ResultModel("account/coupon/redeem/success", "account/coupon/redeem/prompt");

		String encodedAccountNumber = request.getParameter("account");
		int accountNumber = 0;
		if (encodedAccountNumber != null)
			try {
				accountNumber = CacheManager.getEncryptor().decryptIntUrlSafe(encodedAccountNumber);
			} catch (UnsupportedEncodingException e) {
				DevLogger.error("Exception decrypting ID in CouponController", e);
			} catch (IOException e) {
				DevLogger.error("Exception decrypting ID in CouponController", e);
			}

		try {

			coupon = couponManager.getCouponByCode(coupon.getCouponCode());
			if (coupon != null)
				DevLogger.log("received coupon: " + coupon.toFormattedString());

			couponValidator.validate(coupon, accountNumber, result);
			if (result.hasErrors()) {
				return model.getError();
			} else {
				try {
					Account account = accountManager.getAccount(accountNumber);
					ServiceInstance serviceInstance = null;

					if (account != null && account.getServiceinstancelist() != null && account.getServiceinstancelist().size() > 0)
						serviceInstance = account.getServiceinstancelist().get(0);

					if (serviceInstance != null)
						couponManager.applyCoupon(coupon, user, account, serviceInstance);
					else
						return model.getAccessException();

				} catch (AccountManagementException e) {
					model.getAccessException();
				}

				AccountDetail accountDetail = null;
				for (AccountDetail ad : accountDetails)
					if (ad.getAccount().getAccountNo() == accountNumber)
						accountDetail = ad;

				model.addAttribute("accountDetail", accountDetail);
				model.addAttribute("coupon", coupon);
				return model.getSuccess();
			}
		} catch (CouponManagementException e) {
			return model.getAccessException();
		}
	}
}