<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<div class="span12">
  <h3>Your Previous Session Has Expired</h3>
  <p>
    Your previous session has expired.<br /> <b>You will be redirected in <span id="counter">5</span> seconds to a valid session
    </b>
  </p>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$("span#counter").countDown();
	});

	$.fn.countDown = function() {
		$(this).css("color", "#4A87F0").css("font-weight", "bold").css("font-size", "1.2em");
		var counter = $(this);
		var count = $(this).html();
		var targetUrl = '<spring:url value="/" />';
		setInterval(function() {
			$(counter).html(count);
			if (count == 0) {
				window.location = targetUrl;
			}
			count--;
		}, 1000);
	};
</script>

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>