package com.trc.web.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

@Component
public class JCaptchaValidator {

	public static void validate(
			HttpServletRequest request,
			Errors errors) { 
		if (!SimpleImageCaptchaServlet.validateResponse(request, request.getParameter("jcaptcha"))) {
			errors.rejectValue("jcaptcha", "jcaptcha.incorrect", "Image Verificaiton failed");
		}
	}

}