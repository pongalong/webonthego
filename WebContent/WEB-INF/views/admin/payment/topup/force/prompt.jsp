<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form method="post">
  <fieldset>
    <legend>Force Topup</legend>

    <div class="alert alert-info">
      <p>You are about to force a topup on the user for their chosen topup amount. This will take a payment and attempt to restore service if the device is
        suspended. If the device is disconnected, no restoration will be done.</p>
      <ul class="info">
        <li><span>User:</span> ${USER.email}</li>
        <li><span>Device:</span> ${accountDetail.deviceInfo.label}</li>
        <li><span>Amount:</span> &#36;${topupAmount}</li>
      </ul>
    </div>

    <div>
      <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
      <button type="submit" class="button">Force Topup</button>
    </div>
  </fieldset>

</form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>