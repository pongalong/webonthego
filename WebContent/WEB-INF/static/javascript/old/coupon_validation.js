var couponAjaxBufferTime = 1;
var currentCouponAjaxRequest = null;

// $(function() {
// $("#couponCode").keyup(function(e) {
// couponAjaxBufferTime = 1;
// if (currentCouponAjaxRequest == null) {
// currentCouponAjaxRequest = "pending";
// makeBufferedCouponAjaxCall();
// }
// });
// });
//
// function makeBufferedCouponAjaxCall() {
// var couponCode = $("#couponCode").val();
// if (currentCouponAjaxRequest == null) {
// validateCoupon(couponCode);
// } else {
// makeDelayedCouponAjaxCall();
// }
// }
//
// function makeDelayedCouponAjaxCall() {
// setTimeout(function() {
// makeBufferedCouponAjaxCall_recurse();
// }, 500);
// }
//
// function makeBufferedCouponAjaxCall_recurse() {
// couponAjaxBufferTime = couponAjaxBufferTime - 1;
// if (couponAjaxBufferTime == 0) {
// currentCouponAjaxRequest = null;
// }
// makeBufferedCouponAjaxCall();
// }
//
// function validateCoupon(couponCode) {
// $.getJSON("coupons/validate", {
// couponCode : couponCode
// }, function(couponResponse) {
// if (couponResponse.valid) {
// fieldValidated("couponCode", {
// valid : true,
// message : couponResponse.description
// });
// } else {
// fieldValidated("couponCode", {
// valid : false,
// message : couponCode + " is not a valid coupon"
// });
// }
// });
// currentCouponAjaxRequest = null;
// }
//
// function fieldValidated(field, result) {
// var obj = $("#" + field);
// var alertErrorRow = $("div.alert.error").parent("div.row");
// if (result.valid) {
// $(alertErrorRow).fadeOut("slow");
// $(obj).removeClass("validationFailed");
// $(obj).css("color", "green");
// $(obj).css("background", "#eee");
// $("#couponMessage").html(result.message);
// } else {
// $(obj).addClass("validationFailed");
// $("#couponMessage").html(result.message);
// }
// }