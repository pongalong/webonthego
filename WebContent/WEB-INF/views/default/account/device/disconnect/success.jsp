<%@ include file="/WEB-INF/views/default/include/header/headerAndMenu.jsp"%>

<h3>Device Disconnected</h3>

<p>You have successfully deactivated "${accountDetail.deviceInfo.label}".</p>

<a class="button" href="<spring:url value="/devices" />">Ok</a>

<%@ include file="/WEB-INF/views/default/include/footer/footerAndMenu.jsp"%>