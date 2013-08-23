package com.tscp.mvna.domain.controller;

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
import com.trc.user.account.AccountDetailCollection;
import com.tscp.mvna.web.controller.model.ClientPageView;

@Controller
@RequestMapping("/account")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER",
		"AccountDetailCollection",
		"PaymentHistory" })
public class AccountController {
	@Autowired
	private AccountManager accountManager;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showOverview() {
		return new ClientPageView("account/overview");
	}

	@RequestMapping(value = "activity", method = RequestMethod.GET)
	public ModelAndView showActivity(
			@ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails) {

		ClientPageView view = new ClientPageView("account/activity");

		if (accountDetails != null && !accountDetails.isEmpty()) {
			view.setViewName("account/activity/" + accountDetails.get(0).getEncodedAccountNum() + "/1");
			return view.redirect();
		}
		return view;
	}

	@RequestMapping(value = "activity/{encodedAccountNum}", method = RequestMethod.GET)
	public ModelAndView showAccountActivity(
			@PathVariable("encodedAccountNum") String encodedAccountNum) {
		return new ClientPageView("account/activity/" + encodedAccountNum + "/1").redirect();
	}

	@RequestMapping(value = "activity/{encodedAccountNum}/{page}", method = RequestMethod.GET)
	public ModelAndView showAccountActivity(
			@ModelAttribute("USER") User user, @ModelAttribute("AccountDetailCollection") AccountDetailCollection accountDetails, @PathVariable("encodedAccountNum") String encodedAccountNum, @PathVariable("page") int page) {

		AccountDetail accountDetail = accountDetails.find(encodedAccountNum);
		if (accountDetail != null)
			accountDetail.getUsageHistory().setPageNum(page);

		ClientPageView view = new ClientPageView("account/activity");
		view.addObject("accountDetail", accountDetail);
		return view;
	}

}