package com.trc.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.AccountManager;
import com.trc.manager.UserManager;
import com.trc.security.encryption.SessionEncrypter;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.user.account.Overview;
import com.trc.web.model.ResultModel;

@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private UserManager userManager;
	@Autowired
	private AccountManager accountManager;

	@RequestMapping(value = { "", "/", "manage" }, method = RequestMethod.GET)
	public ModelAndView showOverview() {
		ResultModel model = new ResultModel("account/overview");
		User user = userManager.getCurrentUser();
		Overview overview = accountManager.getOverview(user).encodeAccountNo();
		model.addObject("accountDetails", overview.getAccountDetails());
		model.addObject("paymentHistory", overview.getPaymentDetails());
		return model.getSuccess();
	}

	@RequestMapping(value = "activity", method = RequestMethod.GET)
	public ModelAndView showActivity() {
		ResultModel model = new ResultModel("account/activity");
		User user = userManager.getCurrentUser();
		Overview overview = accountManager.getOverview(user).encodeAccountNo();

		String encodedAccountNum;
		if (!overview.getAccountDetails().isEmpty()) {
			encodedAccountNum = overview.getAccountDetails().get(0).getEncodedAccountNum();
			model.setSuccessViewName("redirect:/account/activity/" + encodedAccountNum + "/1");
		}

		return model.getSuccess();

	}

	@RequestMapping(value = "activity/{encodedAccountNum}", method = RequestMethod.GET)
	public String showAccountActivity(@PathVariable("encodedAccountNum") String encodedAccountNum) {
		return "redirect:/account/activity/" + encodedAccountNum + "/1";
	}

	@RequestMapping(value = "activity/{encodedAccountNum}/{page}", method = RequestMethod.GET)
	public ModelAndView showAccountActivity(@PathVariable("encodedAccountNum") String encodedAccountNum, @PathVariable("page") int page) {
		ResultModel model = new ResultModel("account/activity");
		User user = userManager.getCurrentUser();
		Overview overview = accountManager.getOverview(user);
		int accountNum = SessionEncrypter.decryptId(encodedAccountNum);
		List<AccountDetail> accountList = overview.getAccountDetails();

		AccountDetail accountDetail = overview.getAccountDetail(accountNum);
		if (accountDetail.getUsageHistory() != null)
			accountDetail.getUsageHistory().setCurrentPageNum(page);

		model.addObject("accountList", accountList);
		model.addObject("accountDetail", accountDetail);

		return model.getSuccess();
	}

}