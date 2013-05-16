<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Topup completed</h3>

  <div class="success">
    <p>Topup successfully processed!</p>
    <ul class="info">
      <li><span>User:</span> ${USER.email}</li>
      <li><span>Device:</span> ${accountDetail.deviceInfo.label}</li>
      <li><span>Amount:</span> &#36;${topupAmount}</li>
      <li><span>Response:</span> ${paymentResponse.confdescr}</li>
      <li><span>AuthCode:</span> ${paymentResponse.authcode}</li>
    </ul>
  </div>

  <div class="buttons" style="text-align: right;">
    <a class="mBtn" href="<spring:url value="/devices" />">OK</a>
  </div>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>