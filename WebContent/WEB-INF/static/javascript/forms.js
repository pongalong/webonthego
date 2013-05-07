/*******************************************************************************
 * Radio Button Effects to be removed - addCoupon.js still uses some of these
 ******************************************************************************/
function highlightRadio(name, val) {
	var radioButtons = $("input[name='" + name + "']:radio");
	var selectedRadio = $("input[value='" + val + "']");
	$(selectedRadio).attr("checked", true);
	$(radioButtons).selectRadioFromList($(selectedRadio));
}

$.fn.fadeInRadio = function() {
	var label = $(this).next("span");
	$(label).css("font-style", "italic").css("color", "#0067B2").fadeTo("slow", 1.0);
};

$.fn.fadeOutRadio = function() {
	var label = $(this).next("span");
	$(label).css("font-style", "").css("color", "").fadeTo("slow", 0.5);
};

$.fn.selectRadio = function(radioButtons) {
	$(this).fadeInRadio();
	$(radioButtons).not($(this)).each(function() {
		$(this).fadeOutRadio();
	});
};

$.fn.selectRadioFromList = function(selected) {
	$(selected).fadeInRadio();
	$(this).not($(selected)).each(function() {
		$(this).fadeOutRadio();
	});
};

/*******************************************************************************
 * Toggle Slide Effects: drop down and bounce effect for divs
 ******************************************************************************/
$.fn.toggleSlideDownFullHeight = function() {
	if (!$(this).is(":visible")) {
		$(this).css("height", $(window).height() - 200);
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
 * Tooltip Effects
 ******************************************************************************/

$.fn.enableTooltip = function() {
	$(this).focus(function() {
		$(this).next(".tooltip").fadeIn();
	});
	$(this).blur(function() {
		$(this).next(".tooltip").fadeOut();
	});
	return $(this);
};

/*******************************************************************************
 * Caption Effects
 ******************************************************************************/
$.fn.enableCaption = function() {
	var caption = $(this).attr("placeholder");
	var val = $(this).val();

	if (val.length == 0)
		val = caption;

	$(this).css("color", "gray").val(val).focus(function() {
		if ($(this).val() == caption)
			$(this).css("color", "").val("");
	}).blur(function() {
		if ($(this).val() == "") {
			$(this).css("color", "gray");
			$(this).showCaption(caption);
		}
	});
	return $(this);
};

$.fn.showCaption = function(caption) {
	if (caption != null)
		type($(this), caption, 0);
	else
		type($(this), $(this).attr("placeholder"), 0);
};

/**
 * Type-writer text effect
 * 
 * @param obj
 * @param caption
 * @param count
 */
function type(obj, caption, count) {
	$(obj).val(caption.substr(0, count++));
	if (count < caption.length + 1)
		setTimeout(function() {
			type(obj, caption, count);
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
function highlightCard(cardNumber) {
	if (cardNumber.length > 0) {
		var cardType = cardNumber.substring(0, 1);
		var cvv = document.getElementById("verificationcode");
		if (mod10(cardNumber)) {
			if (cardType == 4) {
				cvv.maxLength = 3;
				blurAllExcept($("#ImgVisa"));
			} else if (cardType == "5") {
				cvv.maxLength = 3;
				blurAllExcept($("#ImgMastercard"));
			} else if (cardType == "3") {
				cvv.maxLength = 4;
				blurAllExcept($("#ImgAmex"));
			} else if (cardType == "6") {
				cvv.maxLength = 3;
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
	$("#creditCardImages").find("img").fadeTo("fast", 0.3);
	if (cardImgObj != "unknown") {
		$(cardImgObj).fadeTo("fast", 1).css("border", "1px solid blue");
	}
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
			// 0-9 or numpad 0-9, disallow shift/ctrl/alt
			// check textbox value and tab over if necessary
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