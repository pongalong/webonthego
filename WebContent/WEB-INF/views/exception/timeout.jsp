<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <script type="text/javascript">
			$(document).ready(function() {
				$("span#counter").countDown();
			});

			$.fn.countDown = function() {
				$(this).css("color", "#4A87F0").css("font-weight", "bold").css("font-size", "1.2em");
				var counter = $(this);
				var count = $(this).html();
				var targetUrl = '<spring:url value="/manage" />';
				setInterval(function() {
					$(counter).html(count);
					if (count == 0) {
						window.location = targetUrl;
					}
					count--;
				}, 1000);
			};
		</script>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Your Previous Session Has Expired</h3>
        <p>
          Your previous session has expired.<br /> <b>You will be redirected in <span id="counter">5</span> seconds to a valid session
          </b>
        </p>
        <p>Or you can use the navigation links on the right side of the page.</p>
      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>
    <!-- Close main-content -->

  </div>
  <!-- Close container -->
  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>