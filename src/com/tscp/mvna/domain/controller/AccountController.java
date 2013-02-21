package com.tscp.mvna.domain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.trc.manager.AccountManager;
import com.trc.user.User;
import com.trc.user.account.AccountDetail;
import com.trc.web.model.ResultModel;
import com.trc.web.session.cache.CachedAttributeNotFound;

@Controller
@RequestMapping("/account")
@SessionAttributes({ "USER", "CONTROLLING_USER", "ACCOUNT_DETAILS", "PAYMENT_HISTORY" })
public class AccountController {
	@Autowired
	private AccountManager accountManager;

	@RequestMapping(method = RequestMethod.GET)
	public String showOverview() {
		return "account/overview";
	}

	@RequestMapping(value = "activity", method = RequestMethod.GET)
	public ModelAndView showActivity(
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails) {

		ResultModel model = new ResultModel("account/activity");

		if (accountDetails != null && !accountDetails.isEmpty())
			model.setSuccessViewName("redirect:/account/activity/" + accountDetails.get(0).getEncodedAccountNum() + "/1");
		return model.getSuccess();
	}

	@RequestMapping(value = "activity/{encodedAccountNum}", method = RequestMethod.GET)
	public String showAccountActivity(
			@PathVariable("encodedAccountNum") String encodedAccountNum) {
		return "redirect:/account/activity/" + encodedAccountNum + "/1";
	}

	@RequestMapping(value = "activity/{encodedAccountNum}/{page}", method = RequestMethod.GET)
	public ModelAndView showAccountActivity(
			@ModelAttribute("USER") User user,
			@ModelAttribute("ACCOUNT_DETAILS") List<AccountDetail> accountDetails,
			@PathVariable("encodedAccountNum") String encodedAccountNum,
			@PathVariable("page") int page) {

		ResultModel model = new ResultModel("account/activity");

		AccountDetail ad = getAccountDetailFromSession(accountDetails, encodedAccountNum);
		ad.getUsageHistory().setCurrentPageNum(page);

		model.addAttribute("accountDetail", ad);

		return model.getSuccess();
	}

	/* ****************************************************************************************************************
	 * Helper Methods
	 * ****************************************************************************************************************
	 */

	private AccountDetail getAccountDetailFromSession(
			List<AccountDetail> accountDetails,
			String encodedAccountNum) throws CachedAttributeNotFound {

		for (AccountDetail ad : accountDetails)
			if (ad.getEncodedAccountNum().equals(encodedAccountNum))
				return ad;

		throw new CachedAttributeNotFound("Could not find AccountDetail");
	}

}