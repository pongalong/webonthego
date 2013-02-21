
<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <form:form id="prompt_complete_account" cssClass="validatedForm" method="post">

    <h3>Complete Your Account</h3>

    <p>You have an incomplete activation with a payment on file. Your activation will now resume.</p>

    <!-- Buttons -->
    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue"></input>
    </div>

  </form:form>

</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>