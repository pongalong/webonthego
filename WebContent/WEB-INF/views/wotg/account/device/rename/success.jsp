<%@ include file="/WEB-INF/views/wotg/include/header/headerAndMenu.jsp"%>

<h3>Device Renamed</h3>

<p>Your device "${oldLabel}" has been renamed to "${newLabel}".</p>

<a class="button" href="<spring:url value="/devices" />">Continue </a>


<%@ include file="/WEB-INF/views/wotg/include/footer/footerAndMenu.jsp"%>