<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<form id="queueTopup" method="POST" class="validatedForm">
  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Force Topup</h3>

  <div class="notice">
    <p>You are about to force a topup on the user for their chosen topup amount. This will take a payment and attempt to restore service if the device is
      suspended. If the device is disconnected, no restoration will be done.</p>
    <ul class="info">
      <li><span>User:</span> ${USER.email}</li>
      <li><span>Device:</span> ${accountDetail.deviceInfo.label}</li>
      <li><span>Amount:</span> &#36;${topupAmount}</li>
    </ul>
  </div>

  <div class="buttons">
    <a class="mBtn" href="<spring:url value="/devices" />">Cancel</a> <input type="submit" name="_eventId_submit" value="Confirm" />
  </div>

</form>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>