$(function() {
	$("#deviceSelect").change(function() {
		$("#curtain").fadeIn("fast").center().height($(document).height());
		$("#centerPopup").fadeIn("fast").center();
		var location = '/account/activity/' + $("#deviceSelect option:selected").val();
		window.location.href = location;
	});
	var deviceId = window.location.href.substring(0, window.location.href.lastIndexOf("/"));
	deviceId = deviceId.substring(deviceId.lastIndexOf("/") + 1);
	$("#deviceSelect").val(deviceId);
});