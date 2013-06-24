package com.tscp.mvna.domain.payment.coupon;

import java.io.Serializable;

import com.trc.user.User;
import com.tscp.mvne.Account;

public class CouponRequest implements Serializable {
	private static final long serialVersionUID = 7360898706142303065L;
	private User user;
	private Account account;
	private Coupon coupon;

	public User getUser() {
		return user;
	}

	public void setUser(
			User user) {
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(
			Account account) {
		this.account = account;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(
			Coupon coupon) {
		this.coupon = coupon;
	}

}