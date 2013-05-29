<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form method="POST">
  <fieldset>
    <legend>Queue Topup</legend>

    <div class="alert alert-info">
      <p>This will put the user back on the topup queue which runs approximately every 15 minutes. This can be used to attempt a topup on the next run if
        the customer has updated their payment method but hasn't been billed yet. If the customer's device is suspended, this will restore it. If the device is
        disconnected, no restoration can occur.</p>

      <ul class="info">
        <li><span>User:</span> ${USER.email}</li>
      </ul>
    </div>

    <div>
      <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
      <button type="submit" class="button">Queue</button>
    </div>

  </fieldset>
</form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>