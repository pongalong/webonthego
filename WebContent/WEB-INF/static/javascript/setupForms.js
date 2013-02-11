$(function() {
	var $forms = $("form");
	$.each($forms, function() {
		$(this).submit(function() {
			$("#curtain").fadeIn("fast").center().height($(document).height());
			$("#centerPopup").fadeIn("fast").center();
		});
	});
});