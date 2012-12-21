$(function() {
	$("#creatorIdCheckbox").change(function() {
		if ($(this).is(":checked")) {
			$("#creatorId").val($(this).val());
		} else {
			$("#creatorId").val(0);
		}
	});
	$("#assigneeIdCheckbox").change(function() {
		if ($(this).is(":checked")) {
			$("#assigneeId").val($(this).val());
		} else {
			$("#assigneeId").val(0);
		}
	});
});