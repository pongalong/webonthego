<%@ include file="/WEB-INF/views/trc/include/header/headerAndMenu.jsp"%>

<h3>Device Suspended</h3>

<p>You have successfully suspended "${accountDetail.deviceInfo.label}".</p>

<a class="button" href="<spring:url value="/devices" />">Ok</a>

<%@ include file="/WEB-INF/views/trc/include/footer/footerAndMenu.jsp"%>