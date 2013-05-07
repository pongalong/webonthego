function reloadImage(target, imageUrl) {
	imageUrl = imageUrl + "?call=" + Math.floor(Math.random() * 100);
	$(target).fadeTo("fast", 0, function() {
		$(target).attr("src", imageUrl);
		$(target).fadeTo("fast", 1);
	});
	return false;
}

$(function() {
	$(".captchaReload").click(function(e) {
		e.preventDefault();
		reloadImage($("#jCaptchaImage"), "/static/images/jcaptcha.jpg");
	});
});