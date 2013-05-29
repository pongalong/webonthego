<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Topup Queued</h3>

<div class="alert alert-success">
  <p>User has been queued for topup! Topups occur approximately every 15 minutes.</p>
  <ul class="info">
    <li><span>User:</span> ${USER.email}</li>
  </ul>
</div>

<div>
  <a class="button" href="<spring:url value="/devices" />">OK</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>