package com.trc.web.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

@Component
public class CaptchaValidator {

	public static void validate(
			HttpServletRequest request,
			Errors errors) {
		if (!SimpleImageCaptchaServlet.validateResponse(request, request.getParameter("captcha.value"))) {
			errors.rejectValue("captcha.value", "jcaptcha.incorrect", "Image Verificaiton failed");
		}
	}

}