<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Topup failed</h3>

<div class="alert alert-error">
  <p>Topup could not be processed!</p>
  <ul class="info">
    <li><span>User:</span> ${USER.email}</li>
    <li><span>Device:</span> ${accountDetail.deviceInfo.label}</li>
    <li><span>Amount:</span> &#36;${topupAmount}</li>
    <li><span>AuthCode:</span> ${paymentResponse.authcode}</li>
    <li><span>ConfCode:</span> ${paymentResponse.confcode}</li>
    <li><span>Additional:</span> ${paymentResponse.confdescr}</li>
  </ul>
</div>

<div>
  <a class="button" href="<spring:url value="/devices" />">OK</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>