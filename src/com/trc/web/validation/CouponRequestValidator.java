package com.trc.web.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.coupon.Coupon;
import com.trc.coupon.CouponRequest;
import com.trc.coupon.UserCoupon;
import com.trc.exception.ValidationException;
import com.trc.exception.management.CouponManagementException;
import com.trc.manager.CouponManager;
import com.tscp.mvne.Account;

@Component
public class CouponRequestValidator implements Validator {
	private static final int UNLIMITED_ACCOUNT = -1;
	@Autowired
	private CouponManager couponManager;
	@Autowired
	private CouponValidator couponValidator;

	@Override
	public boolean supports(
			Class<?> myClass) {
		return CouponRequest.class.isAssignableFrom(myClass);
	}

	@Override
	public void validate(
			Object target, Errors errors) {

		CouponRequest couponRequest = (CouponRequest) target;

		errors.pushNestedPath("coupon");
		couponValidator.validate(couponRequest.getCoupon(), errors);
		errors.popNestedPath();
		checkAccount(couponRequest.getAccount(), errors);
		if (!errors.hasErrors())
			checkApplied(couponRequest, errors);
		if (!errors.hasErrors())
			checkLimit(couponRequest, errors);

	}

	protected void checkAccount(
			Account account, Errors errors) {
		if (account.getAccountNo() <= 0)
			errors.rejectValue("account.accountNo", "coupon.device.required", "You must choose a device to apply the coupon to");
	}

	private void checkApplied(
			CouponRequest couponRequest, Errors errors) {
		try {
			if (isApplied(couponRequest))
				errors.reject("coupon.applied", "Coupon has already been applied to your account");
		} catch (ValidationException e) {
			// this error is caught by checkCouponCode
		}
	}

	private void checkLimit(
			CouponRequest couponRequest, Errors errors) {
		if (isAtAccountLimit(couponRequest))
			errors.reject("coupon.limit", "You cannot apply anymore coupons of this type");
	}

	public boolean isApplied(
			CouponRequest couponRequest) throws ValidationException {
		try {
			UserCoupon userCoupon = couponManager.getUserCoupon(new UserCoupon(couponRequest.getCoupon(), couponRequest.getUser(), couponRequest.getAccount()));
			return userCoupon != null;
		} catch (CouponManagementException e) {
			throw new ValidationException(e.getMessage(), e.getCause());
		}
	}

	public boolean isAtAccountLimit(
			CouponRequest couponRequest) {
		int limit = couponRequest.getCoupon().getCouponDetail().getAccountLimit() == null ? UNLIMITED_ACCOUNT : couponRequest.getCoupon().getCouponDetail().getAccountLimit();
		if (isUnlmited(limit)) {
			return false;
		}
		int count = 0;
		try {
			List<UserCoupon> userCoupons = couponManager.getUserCoupons(couponRequest.getUser().getUserId());
			int userCouponType;
			int pendingCouponType;
			int userCouponAccount;
			for (UserCoupon userCoupon : userCoupons) {
				userCouponType = userCoupon.getId().getCoupon().getCouponDetail().getDetailType().getDetailType();
				pendingCouponType = couponRequest.getCoupon().getCouponDetail().getDetailType().getDetailType();
				userCouponAccount = userCoupon.getId().getAccountNumber();
				if ((userCouponType == pendingCouponType) && userCouponAccount == couponRequest.getAccount().getAccountNo()) {
					count++;
				}
			}
			return count >= limit;
		} catch (CouponManagementException e) {
			return true;
		}
	}

	protected boolean isUnlmited(
			int accountLimit) {
		return accountLimit == UNLIMITED_ACCOUNT;
	}

	public boolean isEligible(
			CouponRequest couponRequest) {
		try {
			boolean isAvailable = couponValidator.isQuantityAvailable(couponRequest.getCoupon());
			boolean isApplied = isApplied(couponRequest);
			boolean isAtAccountLimit = isAtAccountLimit(couponRequest);
			boolean isExistingStackable;
			boolean isStackable;
			List<UserCoupon> userCoupons = couponManager.getUserCoupons(couponRequest.getUser().getUserId());
			Coupon existingCoupon;
			for (UserCoupon uc : userCoupons) {
				existingCoupon = uc.getId().getCoupon();
				isExistingStackable = existingCoupon.isStackable();
				if (isExistingStackable) {
					isStackable = couponValidator.isStackable(existingCoupon, couponRequest.getCoupon());
				} else {
					isStackable = couponValidator.isStackable(couponRequest.getCoupon(), existingCoupon);
				}
				if (!isStackable) {
					return false;
				}
			}
			boolean result = isAvailable && !isApplied && !isAtAccountLimit;
			return result;
		} catch (CouponManagementException e) {
			return false;
		} catch (ValidationException e) {
			return false;
		}
	}

}
