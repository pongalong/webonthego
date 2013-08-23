package com.trc.user.account;

import com.tscp.mvna.web.session.cache.CacheManager;
import com.tscp.mvna.web.session.cache.Cacheable;
import com.tscp.mvne.Account;
import com.tscp.mvne.Device;

/**
 * AccountDetail contains all information for a given account.
 * 
 * @author Tachikoma
 * 
 */
public class AccountDetail implements Cacheable {
	private Account account;
	private UsageHistory usageHistory;
	private Device device;
	private String topUp;
	private String encodedDeviceId;
	private String encodedAccountNum;

	/* ****************************************
	 * Getter and Setter Methods
	 */

	public String getEncodedAccountNum() {
		return encodedAccountNum;
	}

	public void setEncodedAccountNum(
			String encodedAccountNum) {
		this.encodedAccountNum = encodedAccountNum;
	}

	public String getEncodedDeviceId() {
		return encodedDeviceId;
	}

	public void setEncodedDeviceId(
			String encodedDeviceId) {
		this.encodedDeviceId = encodedDeviceId;
	}

	public String getTopUp() {
		return topUp;
	}

	public void setTopUp(
			String topUp) {
		this.topUp = topUp;
	}

	public UsageHistory getUsageHistory() {
		return usageHistory;
	}

	public void setUsageHistory(
			UsageHistory usageHistory) {
		this.usageHistory = usageHistory;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(
			Account account) {
		this.account = account;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(
			Device deviceInfo) {
		this.device = deviceInfo;
	}

	/* ****************************************
	 * Cacheable Methods
	 */

	private long cachedTime = System.currentTimeMillis();
	private boolean invalidated;

	@Override
	public boolean isStale() {
		return System.currentTimeMillis() - cachedTime > CacheManager.expirationTime;
	}

	@Override
	public boolean isInvalidated() {
		return invalidated;
	}

	public void setInvalidated(
			boolean invalidated) {
		this.invalidated = invalidated;
	}

	@Override
	public void updateCachedTime() {
		cachedTime = System.currentTimeMillis();
	}

	@Override
	public String getCacheKey() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean equals(
			Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountDetail other = (AccountDetail) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (account.getAccountNo() != other.account.getAccountNo())
			return false;
		if (device == null) {
			if (other.device != null)
				return false;
		} else if (device.getId() != other.device.getId())
			return false;
		return true;
	}

	/* ****************************************
	 * Debug Methods
	 */

	@Override
	public String toString() {
		Integer accountNo = account == null ? null : account.getAccountNo();
		Integer deviceId = device == null ? null : device.getId();
		return "AccountDetail [account=" + accountNo + ", deviceInfo=" + deviceId + "]";
	}

}