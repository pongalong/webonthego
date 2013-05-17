<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Ticket Submitted</h3>
<p>We will investigate the issue and get back to you as soon as possible. Thank You!</p>
<p>Ticket ID: ${ticket.id}</p>
<a href="<spring:url value="/" />" class="mBtn">Done</a>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>