package com.trc.web.validation;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.trc.coupon.Coupon;
import com.trc.coupon.CouponStackable;
import com.trc.exception.management.CouponManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.CouponManager;
import com.trc.manager.UserManager;

@Component
public class CouponValidator implements Validator {

	@Autowired
	protected CouponManager couponManager;
	@Autowired
	protected UserManager userManager;
	@Autowired
	protected AccountManager accountManager;

	@Override
	public boolean supports(
			Class<?> myClass) {
		return Coupon.class.isAssignableFrom(myClass);
	}

	/**
	 * This is currently used by webflow only. It skips validation if no coupon is entered.
	 */
	@Override
	public void validate(
			Object target, Errors errors) {
		Coupon coupon = (Coupon) target;
		checkExists(coupon, errors);
		if (!errors.hasErrors()) {
			checkCouponCode(coupon.getCouponCode(), errors);
			checkStartDate(coupon.getStartDate(), errors);
			checkEndDate(coupon.getEndDate(), errors);
			checkQuantity(coupon, errors);
		}
	}

	protected void checkCouponCode(
			String couponCode, Errors errors) {
		if (couponCode == null || couponCode.length() < 3)
			errors.rejectValue("couponCode", "coupon.code.invalid", "Not a valid coupon code");
	}

	protected void checkEndDate(
			Date endDate, Errors errors) {
		if (endDate != null && endDate.compareTo(new Date()) <= 0)
			errors.rejectValue("endDate", "coupon.date.expired", "Coupon is expired");
	}

	protected void checkStartDate(
			Date startDate, Errors errors) {
		if (startDate != null && startDate.compareTo(new Date()) >= 0)
			errors.rejectValue("startDate", "coupon.date.notActive", "Coupon has not started");
	}

	protected void checkQuantity(
			Coupon coupon, Errors errors) {
		if (coupon.getQuantity() > 0 && (coupon.getQuantity() - coupon.getUsed()) < 1)
			errors.rejectValue("quantity", "coupon.quantity.insufficient", "This coupon has been exhausted");
	}

	protected void checkExists(
			Coupon coupon, Errors errors) {
		if (coupon == null) {
			errors.rejectValue("couponCode", "coupon.code.invalid", "Not a valid coupon code");
		} else if (coupon.isEmpty()) {
			errors.rejectValue("couponCode", "coupon.code.required", "You must enter a coupon code");
		} else {
			try {
				Coupon fetchedCoupon = couponManager.getCouponByCode(coupon.getCouponCode());
				if (fetchedCoupon == null || fetchedCoupon.isEmpty()) {
					errors.rejectValue("couponCode", "coupon.code.invalid", "Not a valid coupon code");
				} else {
					coupon = fetchedCoupon;
				}
			} catch (CouponManagementException e) {
				errors.rejectValue("couponCode", "coupon.code.invalid", "Not a valid coupon code");
			}
		}
	}

	public boolean isStackable(
			Coupon stackableCoupon, Coupon candidate) {
		Collection<CouponStackable> stackable = stackableCoupon.getCouponDetail().getStackable();
		for (CouponStackable cs : stackable) {
			if (cs.getId().getStackableCouponDetailId() == candidate.getCouponDetail().getCouponDetailId()) {
				return true;
			}
		}
		return false;
	}

	public boolean isQuantityAvailable(
			Coupon coupon) {
		return (coupon.getQuantity() - coupon.getUsed()) > 0;
	}

}