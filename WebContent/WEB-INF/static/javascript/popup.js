function openPrompt(name) {
	$("#curtain").fadeIn("fast").center().height($(document).height());
	var popup = $("#" + name);
	var raisedCenter = ((($(window).height() - $(popup).outerHeight()) / 3) + $(window).scrollTop() + "px");
	$(popup).fadeIn("fast").center();
	$(popup).css("top", raisedCenter);
}

$(function() {
	$(".closePrompt").click(function() {
		$(this).parents(".prompt").hide();
		$("#curtain").fadeOut("fast");
	});
});

// Adjust height of overlay to fill screen when browser gets resized
$(window).bind("resize", function() {
	$("#curtain").css("height", $(window).height());
	$(".search_results_box").css("height", $(window).height() - 200);

	var prompts = $(".prompt");
	$.each(prompts, function() {
		var vertical = (($(window).height() - $(this).outerHeight()) / 3) + $(window).scrollTop() + "px";
		var horizontal = (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft() + "px";
		$(this).css("top", vertical);
		$(this).css("left", horizontal);
	});

});

$.fn.center = function() {
	this.css("position", "fixed");
	this.css("top", (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + "px");
	this.css("left", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + "px");
	return this;
};