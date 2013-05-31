<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<div class="alert alert-success">
  <h3>Ticket Submitted</h3>

  <p>We will investigate the issue and get back to you as soon as possible. Thank You!</p>

  <ul class="info">
    <li><span>Ticket ID:</span> ${ticket.id}</li>
    <li><span>Email:</span> ${USER.email}</li>
    <c:if test="${CONTROLLING_USER.userId > 0}"><li><span>Created By:</span> ${CONTROLLING_USER.email}</li></c:if>
    <li><span>Description:</span> ${ticket.description}</li>
  </ul>
  
  <div class="button-row">
    <a href="<spring:url value="/" />" class="button">Done</a>
  </div>

</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>