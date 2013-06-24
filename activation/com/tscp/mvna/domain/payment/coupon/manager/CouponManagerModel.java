package com.tscp.mvna.domain.payment.coupon.manager;

import java.util.Collection;

import com.trc.user.User;
import com.tscp.mvna.domain.payment.coupon.Coupon;
import com.tscp.mvna.domain.payment.coupon.CouponRequest;
import com.tscp.mvna.domain.payment.coupon.UserCoupon;
import com.tscp.mvna.domain.payment.coupon.exception.CouponManagementException;
import com.tscp.mvne.Account;
import com.tscp.mvne.ServiceInstance;

public interface CouponManagerModel {

	public Collection<UserCoupon> getUserCoupons(
			int userId) throws CouponManagementException;

	public int applyCoupon(
			CouponRequest couponRequest) throws CouponManagementException;

	public void cancelCoupon(
			Coupon coupon, User user, Account account, ServiceInstance serviceInstance) throws CouponManagementException;

}
