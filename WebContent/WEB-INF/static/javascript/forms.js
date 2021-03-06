$.fn.toggleSlideDownRemainingHeight = function() {
	if (!$(this).is(":visible")) {
		$(this).show();
		var offset = $(this).offset();
		$(this).css("height", $(window).height() - offset.top - 200);
		$(this).hide();
		$(this).slideDown({
			easing : "easeOutBounce",
			duration : 1000
		});
	}
};

$.fn.toggleSlideDown = function() {
	if (!$(this).is(":visible")) {
		$(this).slideDown({
			easing : "easeOutBounce",
			duration : 1000
		});
	}
};

$.fn.toggleSlideUp = function() {
	if ($(this).is(":visible")) {
		$(this).slideUp({
			easing : "easeInQuad",
			duration : 500
		});
	}
};

$.fn.toggleSlide = function() {
	if ($(this).is(":visible")) {
		$(this).toggleSlideUp();
		return false;
	} else {
		$(this).toggleSlideDown();
		return false;
	}
};

/*******************************************************************************
 * Caption Effects
 ******************************************************************************/
function type(target, placeholder) {
	typeToIndex(target, placeholder, 0);
}

function typeToIndex(target, placeholder, index) {
	$(target).val(placeholder.substr(0, index++));
	if (index < placeholder.length + 1)
		setTimeout(function() {
			typeToIndex(target, placeholder, index);
		}, 22);
}

/*******************************************************************************
 * Credit Card Forms
 ******************************************************************************/

/**
 * Highlights the image for the type of credit card entered by calling
 * blurAllExcpet
 * 
 * @returns {Boolean}
 */
function highlightCard(path, cardNumber) {
	if (cardNumber.length > 0) {
		var cardType = cardNumber.substring(0, 1);
		var cvv = $(path + "verificationcode");
		if (mod10(cardNumber)) {
			if (cardType == 4) {
				cvv.attr("maxLength", 3);
				blurAllExcept($("#ImgVisa"));
			} else if (cardType == "5") {
				cvv.attr("maxLength", 3);
				blurAllExcept($("#ImgMastercard"));
			} else if (cardType == "3") {
				cvv.attr("maxLength", 4);
				blurAllExcept($("#ImgAmex"));
			} else if (cardType == "6") {
				cvv.attr("maxLength", 3);
				blurAllExcept($("#ImgDiscover"));
			} else {
				blurAllExcept("unknown");
			}
		} else {
			$("#creditCardImages").find("img").fadeTo("slow", 1);
		}
	}
	return false;
}

/**
 * Blurs all the credit card images except for the given one by lowering opacity
 * 
 * @param cardImgObj
 */
function blurAllExcept(cardImgObj) {
	$("#creditCardImages").find("img").not($(cardImgObj)).fadeTo("fast", 0.3);
}

/*******************************************************************************
 * Input Restrictions
 ******************************************************************************/

/**
 * Only allows numerics to be entered into the field
 */
$(function() {
	$(".numOnly").keydown(function(e) {
		if ((!e.shiftKey && !e.ctrlKey && !e.altKey) && ((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 96 && e.keyCode <= 105))) {
			// 0-9 or numpad 0-9, disallow shift/ctrl/alt, check textbox value and tab
			// over if necessary
		} else if (e.keyCode != 8 && e.keyCode != 9 && e.keyCode != 13 && e.keyCode != 46 && e.keyCode != 37 && e.keyCode != 39) {
			// not esc/del/left/right
			e.preventDefault();
		}
		// else the key should be handled normally
	});
});

/*******************************************************************************
 * Confirm Form Value Effects
 ******************************************************************************/
$.fn.enableConfirmField = function() {
	var confirmationField = $("#" + $(this).attr("id").replace(".value", "\\.confirmValue"));
	confirmationField.enableValidation(validateMatches);
	$(this).blur(function() {
		if ($(this).val().length != 0) {
			if ($(this).prop("requiresValidation")) {
				if ($(this).prop("isValid")) {
					$(this).toggleConfirmationField(true, true);
				}
			} else {
				$(this).toggleConfirmationField(true, true);
			}
		} else {
			$(this).toggleConfirmationField(false, false);
		}
	});

	$(this).change(function() {
		confirmationField.toggleValidationFields(confirmationField.validate(validateMatches));
	});

	return $(this);
};

$.fn.toggleConfirmationField = function(focus, validate) {
	var confirmationField = $("#" + $(this).attr("id").replace(".value", "\\.confirmValue"));
	if (focus)
		confirmationField.focus();
	if (validate)
		confirmationField.toggleValidationFields(confirmationField.validate(validateMatches));
	return $(this);
};

/*******************************************************************************
 * Validation Effects
 ******************************************************************************/
$.fn.toggleValidationFields = function(isValid) {
	var container = $(this).next(".validation");
	var accept = container.children(".accept");
	var reject = container.children(".reject");
	if ($(this).val() != "") {
		if (isValid) {
			accept.fadeIn();
			reject.hide();
		} else {
			reject.fadeIn();
			accept.hide();
		}
	} else {
		accept.hide();
		reject.hide();
	}
	container.fadeIn();
};

$.fn.validate = function(func) {
	var isValid;
	if (func == validateMatches) {
		var parent = $("#" + $(this).attr("id").replace(".confirmValue", "\\.value"));
		isValid = func($(this).val(), parent.val());
	} else {
		isValid = func($(this).val());
	}
	return isValid;
};

$.fn.enableValidation = function(func) {
	$(this).prop("validationFunction", func);
	$(this).prop("requiresValidation", true);
	$(this).blur(function() {
		var isValid = $(this).validate(func);
		$(this).toggleValidationFields(isValid);
		if (isValid) {
			$(this).prop("isValid", true);
		} else {
			$(this).prop("isValid", false);
		}
	});
	return $(this);
};

/*******************************************************************************
 * Validation Methods
 ******************************************************************************/

$.fn.exists = function() {
	return this.length > 0;
};

function validateChecked(value) {
	return value == "true";
}

function validateSelected(value) {
	return value != 0;
}

function validateNotEmpty(value) {
	return value != null && $.trim(value) != "";
}

function validateMatches(value1, value2) {
	return value1 != "" && value1 == value2;
}

function validateAlphaNumeric(value) {
	var alphaNumRegex = /^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i;
	return alphaNumRegex.test(value);
}

function validatePassword(value) {
	return validateAlphaNumeric(value);
}

function validateEmail(email) {
	var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA	-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return emailRegex.test(email);
}