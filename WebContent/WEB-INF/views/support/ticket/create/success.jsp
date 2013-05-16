<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="height: 100%;">
  <h3>Ticket Submitted</h3>

  <div class="info">
    <p>We will investigate the issue and get back to you as soon as possible. Thank You!</p>
    <ul>
      <li><span>Ticket Number:</span> ${ticket.id}</li>
      <li><span>Category:</span> ${ticket.category.description}</li>
      <li><span>Title:</span> ${ticket.title}</li>
    </ul>
  </div>

  <a id="account" href="<spring:url value="/" />" class="mBtn"><span>Done</span></a>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>