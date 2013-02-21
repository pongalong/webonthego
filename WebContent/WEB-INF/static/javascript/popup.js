function openPrompt(name) {
	$("#curtain").fillHeight().center().fadeIn("fast");
	$("#loadingMsg").hide();
	$("#" + name).thirdHeight().centerHoriz().fadeIn("fast");
}

function closePrompt(name) {
	$(name).parents(".prompt").hide();
	$("#curtain").fadeOut("fast");
}

$.fn.center = function() {
	this.css("position", "fixed");
	this.centerVert();
	this.centerHoriz();
	return this;
};

$.fn.centerVert = function() {
	this.css("position", "fixed");
	this.css("top", (($(window).height() - this.outerHeight()) / 2) + $(window).scrollTop() + "px");
	return this;
};

$.fn.centerHoriz = function() {
	this.css("position", "fixed");
	this.css("left", (($(window).width() - this.outerWidth()) / 2) + $(window).scrollLeft() + "px");
	return this;
};

$.fn.fillHeight = function() {
	this.height($(document).height());
	return this;
};

$.fn.thirdHeight = function() {
	this.css("position", "fixed");
	this.css("top", (($(window).height() - this.outerHeight()) / 3) + $(window).scrollLeft() + "px");
	return this;
};

/**
 * Adjust height of overlay and popups to fill screen and center when browser
 * gets resized
 */
$(window).bind("resize", function() {
	$("#curtain").fillHeight();
	$("#loadingMsg").center();
	$(".search_results_box").css("height", $(window).height() - 200);
	var prompts = $(".prompt");
	$.each(prompts, function() {
		$(this).thirdHeight().centerHoriz();
	});
});