<%@ include file="/WEB-INF/views/default/include/header/headerAndMenu.jsp"%>

<h3>Device Renamed</h3>

<p>Your device "${oldLabel}" has been renamed to "${newLabel}".</p>

<a class="button" href="<spring:url value="/devices" />">Continue </a>


<%@ include file="/WEB-INF/views/default/include/footer/footerAndMenu.jsp"%>