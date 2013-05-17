<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3 style="margin-bottom: 10px; padding-bottom: 0px;">Device Disconnected</h3>
<p>You have successfully deactivated "${accountDetail.deviceInfo.label}".</p>
<a class="button action-m" href="<spring:url value="/devices" />">Continue</a>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>