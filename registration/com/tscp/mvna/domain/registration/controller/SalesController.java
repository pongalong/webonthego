package com.tscp.mvna.domain.registration.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/sales")
@PreAuthorize("isAuthenticated() and hasPermission('', 'isInternalUser')")
@SessionAttributes({
		"USER",
		"CONTROLLING_USER" })
public class SalesController {

	@RequestMapping(value = {
			"",
			"/",
			"home" }, method = RequestMethod.GET)
	public String home() {
		return "sales/home";
	}

}