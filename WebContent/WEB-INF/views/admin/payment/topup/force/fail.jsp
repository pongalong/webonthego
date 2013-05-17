<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3 style="margin-bottom: 10px; padding-bottom: 0px;">Topup failed</h3>

<div class="error">
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

<div class="buttons" style="text-align: right;">
  <a class="mBtn" href="<spring:url value="/devices" />">OK</a>
</div>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>