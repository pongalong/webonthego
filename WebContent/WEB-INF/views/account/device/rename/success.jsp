<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3 style="margin-bottom: 10px; padding-bottom: 0px;">Device Renamed</h3>
<p>Your device "${oldLabel}" has been renamed to "${newLabel}".</p>
<p style="margin: 10px 0;">
  <a class="mBtn" href="<spring:url value="/devices" />">Continue </a>
</p>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>