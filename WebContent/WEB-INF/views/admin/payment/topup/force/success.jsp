<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Topup completed</h3>

<div class="alert alert-success">
  <p>Topup successfully processed!</p>
  <ul class="info">
    <li><span>User:</span> ${USER.email}</li>
    <li><span>Device:</span> ${accountDetail.deviceInfo.label}</li>
    <li><span>Amount:</span> &#36;${topupAmount}</li>
    <li><span>Response:</span> ${paymentResponse.confdescr}</li>
    <li><span>AuthCode:</span> ${paymentResponse.authcode}</li>
  </ul>
</div>

<div>
  <a class="button" href="<spring:url value="/devices" />">OK</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>