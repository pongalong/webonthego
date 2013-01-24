<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body onload="setExpirationDate('${creditCard.expirationDate}')">
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18">

        <form:form id="prompt_complete_account" cssClass="validatedForm" method="post" commandName="creditCardPayment">
          <h3>Complete Your Account</h3>

          <p>We'll need some additional information to complete your account before you can activate your device.</p>

          <!-- Buttons -->
          <div class="buttons">
            <a id="prompt_complete_account_button_submit" href="#" class="button action-m"><span>Continue</span> </a> <input id="prompt_complete_account_submit"
              type="submit" name="_eventId_submit" value="Continue" class="hidden"></input>
          </div>
        </form:form>

      </div>
    </div>
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>

</body>
</html>