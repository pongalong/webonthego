/**
 * Setup curtain effect on form submission
 */
$(function() {
	var $forms = $("form");
	$.each($forms, function() {
		$(this).submit(function() {
			$("#curtain").fadeIn("fast");
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

		highlightCard(path, $(path + "creditCardNumber").val());

		var inDate = $(path + "expirationDate").val();
		if (inDate != null && inDate != "") {
			$("#monthSelect").val(inDate.substring(0, 2));
			$("#yearSelect").val(inDate.substring(2));
		}

		$(this).submit(function() {
			$(path + "expirationDate").val($("#monthSelect").val() + $("#yearSelect").val());
		});

		$(path + "creditCardNumber").change(function() {
			highlightCard(path, $(path + "creditCardNumber").val());
		});
	}
});