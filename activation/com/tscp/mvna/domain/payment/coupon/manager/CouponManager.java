package com.tscp.mvna.domain.payment.coupon.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trc.user.User;
import com.trc.web.validation.CouponRequestValidator;
import com.trc.web.validation.CouponValidator;
import com.tscp.mvna.domain.payment.coupon.Coupon;
import com.tscp.mvna.domain.payment.coupon.CouponDetail;
import com.tscp.mvna.domain.payment.coupon.CouponRequest;
import com.tscp.mvna.domain.payment.coupon.UserCoupon;
import com.tscp.mvna.domain.payment.coupon.exception.CouponManagementException;
import com.tscp.mvna.domain.payment.coupon.exception.CouponServiceException;
import com.tscp.mvna.domain.payment.coupon.manager.service.CouponService;
import com.tscp.mvne.Account;
import com.tscp.mvne.ServiceInstance;
import com.tscp.util.logger.LogLevel;
import com.tscp.util.logger.aspect.Loggable;

@Component
public class CouponManager implements CouponManagerModel {
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponValidator couponValidator;
	@Autowired
	private CouponRequestValidator couponRequestValidator;

	@Loggable(value = LogLevel.TRACE)
	public List<UserCoupon> getUserCoupons(
			int userId) throws CouponManagementException {
		try {
			return couponService.getUserCoupons(userId);
		} catch (CouponServiceException e) {
			throw new CouponManagementException("Error getting UserCoupon for user " + userId + ": " + e.getMessage());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public UserCoupon getUserCoupon(
			UserCoupon userCoupon) throws CouponManagementException {
		try {
			return couponService.getUserCoupon(userCoupon);
		} catch (CouponServiceException e) {
			throw new CouponManagementException("Error getting UserCoupon for user " + userCoupon.getId().getUserId() + ": " + e.getMessage());
		}
	}

	public int applyCoupon(
			CouponRequest couponRequest) throws CouponManagementException {

		if (couponRequest.getAccount() == null || couponRequest.getAccount().getServiceinstancelist() == null || couponRequest.getAccount().getServiceinstancelist().isEmpty())
			throw new CouponManagementException("ServiceInstance is required");
		if (couponRequestValidator.isAtAccountLimit(couponRequest))
			throw new CouponManagementException("Coupon " + couponRequest.getCoupon().getCouponId() + " has already been applied to it's limit by user " + couponRequest.getUser().getUserId());

		try {
			if (couponRequest.getCoupon().getCouponDetail().getContract().getContractType() == -1)
				return couponService.applyCouponPayment(couponRequest);
			else
				return couponService.applyCoupon(couponRequest);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}

	}

	@Loggable(value = LogLevel.TRACE)
	public void cancelCoupon(
			Coupon coupon, User user, Account account, ServiceInstance serviceInstance) throws CouponManagementException {
		try {
			couponService.cancelCoupon(user, coupon, account, serviceInstance);
		} catch (CouponServiceException e) {
			// TODO should attempt to cancel again or queue for cancellation
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	/* *****************************************************************
	 * Coupon Management *****************************************************************
	 */

	@Loggable(value = LogLevel.TRACE)
	public int insertCoupon(
			Coupon coupon) throws CouponManagementException {
		try {
			return couponService.insertCoupon(coupon);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public void deleteCoupon(
			Coupon coupon) throws CouponManagementException {
		try {
			couponService.deleteCoupon(coupon);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public void updateCoupon(
			Coupon coupon) throws CouponManagementException {
		try {
			couponService.updateCoupon(coupon);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public Coupon getCoupon(
			int couponId) throws CouponManagementException {
		try {
			return couponService.getCoupon(couponId);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public List<Coupon> getAllCoupons() throws CouponManagementException {
		try {
			return couponService.getAllCoupons();
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public Coupon getCouponByCode(
			String couponCode) throws CouponManagementException {
		try {
			Coupon coupon = couponService.getCouponByCode(couponCode);
			return coupon;
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	/* *****************************************************************
	 * CouponDetail Management *****************************************************************
	 */

	@Loggable(value = LogLevel.TRACE)
	public int insertCouponDetail(
			CouponDetail couponDetail) throws CouponManagementException {
		try {
			return couponService.insertCouponDetail(couponDetail);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public void deleteCouponDetail(
			CouponDetail couponDetail) throws CouponManagementException {
		try {
			couponService.deleteCouponDetail(couponDetail);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public void updateCouponDetail(
			CouponDetail couponDetail) throws CouponManagementException {
		try {
			couponService.updateCouponDetail(couponDetail);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}

	@Loggable(value = LogLevel.TRACE)
	public CouponDetail getCouponDetail(
			int couponDetailId) throws CouponManagementException {
		try {
			return couponService.getCouponDetail(couponDetailId);
		} catch (CouponServiceException e) {
			throw new CouponManagementException(e.getMessage(), e.getCause());
		}
	}
}