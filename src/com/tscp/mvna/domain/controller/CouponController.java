package com.tscp.mvna.domain.controller;

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
import com.trc.coupon.CouponRequest;
import com.trc.coupon.ajax.CouponResponse;
import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.CouponManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.CouponManager;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.web.model.ResultModel;
import com.trc.web.validation.CouponRequestValidator;

@Controller
@RequestMapping("/coupons")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"ACCOUNT_DETAILS",
		"couponRequest",
		"coupon" })
public class CouponController {
	@Autowired
	private CouponManager couponManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private CouponRequestValidator couponRequestValidator;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public @ResponseBody
	CouponResponse validateCoupon(
			@RequestParam String couponCode) {
		try {
			Coupon coupon = couponManager.getCouponByCode(couponCode);
			if (coupon == null)
				return new CouponResponse(false, "Not a valid coupon");
			else if (coupon.getCouponDetail().getContract().getContractType() > 0)
				return new CouponResponse(true, coupon.getCouponDetail().getContract().getDescription() + " for " + coupon.getCouponDetail().getDuration() + " months");
			else
				return new CouponResponse(true, "Credit value of $" + coupon.getCouponDetail().getAmount());
		} catch (CouponManagementException e) {
			return new CouponResponse(false, "Internal error, try again");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showRedeemCoupon() {
		ResultModel model = new ResultModel("account/coupon/redeem/prompt");
		model.addAttribute("couponRequest", new CouponRequest());
		return model.getSuccess();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView postRedeemCoupon(
			HttpServletRequest request, @ModelAttribute("USER") User user, @ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails, @ModelAttribute("couponRequest") CouponRequest couponRequest, BindingResult result) {

		ResultModel model = new ResultModel("account/coupon/redeem/success", "account/coupon/redeem/prompt");

		try {
			couponRequest.setCoupon(couponManager.getCouponByCode(couponRequest.getCoupon().getCouponCode()));
			if (couponRequest.getAccount().getAccountNo() > 0)
				couponRequest.setAccount(accountManager.getAccount(couponRequest.getAccount().getAccountNo()));
			couponRequest.setUser(user);
			couponRequestValidator.validate(couponRequest, result);
		} catch (CouponManagementException | AccountManagementException e) {
			return model.getAccessException();
		}

		if (result.hasErrors())
			return model.getError();
		else
			try {
				couponManager.applyCoupon(couponRequest);

				// update the AccountDetail in session with the updated Account
				AccountDetail accountDetail = null;
				for (AccountDetail ad : accountDetails)
					if (ad.getAccount().getAccountNo() == couponRequest.getAccount().getAccountNo())
						accountDetail = ad;

				model.addAttribute("accountDetail", accountDetail);
				model.addAttribute("coupon", couponRequest.getCoupon());
				return model.getSuccess();
			} catch (CouponManagementException e) {
				return model.getAccessException();
			}
	}
}