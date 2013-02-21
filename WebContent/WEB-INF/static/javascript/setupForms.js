/**
 * Setup login forms and caption effects
 */
$(function() {
	if ($("#j_username").exists()) {
		$("#j_username").enableCaption();
		$("#j_password_holder").enableCaption().focus(function() {
			$(this).addClass("hidden");
			$("#j_password").removeClass("hidden").focus();
		});
		$("#j_password").blur(function() {
			if ($("#j_password").val() == "") {
				$("#j_password").addClass("hidden");
				$("#j_password_holder").removeClass("hidden").showCaption();
			}
		});
	}
});

/**
 * Setup curtain effect on form submission
 */
$(function() {
	var $forms = $("form");
	$.each($forms, function() {
		$(this).submit(function() {
			$("#curtain").fillHeight().center().fadeIn("fast");
			$("#loadingMsg").center().fadeIn("fast");
		});
	});
});

/**
 * Setup credit card forms
 */
$(function() {
	if ($("#creditCard\\.creditCardNumber").exists() || $("#creditCardNumber").exists()) {
		var path = "#";

		if ($("#creditCard\\.creditCardNumber").exists())
			path = "#creditCard\\.";
		else if ($("#creditCardNumber").exists())
			path = "#";

		$("#cvvInfo").hover(function() {
			$(this).next("span.hover_tooltip").show();
		}, function() {
			$(this).next("span.hover_tooltip").hide();
		});

		highlightCard($(path + "creditCardNumber").val());

		var inDate = $(path + "expirationDate").val();
		if (inDate != null && inDate != "") {
			$("#monthSelect").val(inDate.substring(0, 2));
			$("#yearSelect").val(inDate.substring(2));
		}

		$(this).submit(function() {
			$(path + "expirationDate").val($("#monthSelect").val() + $("#yearSelect").val());
		});

		$(path + "creditCardNumber").change(function() {
			highlightCard();
		});
	}
});

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