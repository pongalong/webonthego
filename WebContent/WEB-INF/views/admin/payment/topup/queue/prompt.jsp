<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form id="queueTopup" method="POST" class="validatedForm">
    <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Queue Topup</h3>

    <div class="notice">
      <p>This will put the user back on the topup queue which runs approximately every 15 minutes. This can be used to attempt a topup on the next run if
        the customer has updated their payment method but hasn't been billed yet. If the customer's device is suspended, this will restore it. If the device is
        disconnected, no restoration can occur.</p>

      <ul class="info">
        <li><span>User:</span> ${USER.email}</li>
      </ul>
    </div>

    <div class="buttons">
      <a class="mBtn" href="<spring:url value="/devices" />">Cancel</a> <input type="submit" name="_eventId_submit" value="Confirm" />
    </div>

  </form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>