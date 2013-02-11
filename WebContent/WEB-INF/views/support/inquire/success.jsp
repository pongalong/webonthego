<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Ticket Submitted</h3>
  <p>We will investigate the issue and get back to you as soon as possible. Thank You!</p>
  <p>Ticket ID: ${ticket.id}</p>
  <a href="<spring:url value="/" />" class="mBtn"><span>Done</span></a>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>