<%@ include file="/WEB-INF/views/include/header/header.jsp"%>
<div class="span-18">

  <form:form id="prompt_complete_account" cssClass="validatedForm" method="post" commandName="creditCardPayment">

    <h3>Complete Your Account</h3>

    <p>We'll need some additional information to complete your account before you can activate your device.</p>

    <!-- Buttons -->
    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue"></input>
    </div>

  </form:form>

</div>
<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>