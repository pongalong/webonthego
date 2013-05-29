<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form id="refund" cssClass="form-horizontal" method="POST" commandName="refundRequest">
  <fieldset>
    <legend>Reverse Transaction</legend>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.refundRequest'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="code" />
        <form:errors path="notes" />
        <form:errors path="verified" />
        <form:errors path="jcaptcha" />
        <!-- Global Errors -->
        <spring:bind path="refundRequest">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div id="verifyInfo" class="alert alert-info">
      <p>This reversal has been requested by ${CONTROLLING_USER.username}. Please verify the following information:</p>
      <ul class="info">
        <li><span>User:</span> ${USER.userId}</li>
        <li><span>Email:</span> ${USER.email}</li>
        <li><span>Amount:</span> &#36;<fmt:formatNumber value="${refundRequest.paymentTransaction.paymentAmount}" pattern="0.00" /></li>
        <li><span>Account:</span> ${refundRequest.paymentTransaction.accountNo}</li>
        <li><span>Date:</span> ${refundRequest.paymentTransaction.paymentUnitDate.month}/ ${refundRequest.paymentTransaction.paymentUnitDate.day}/
          ${refundRequest.paymentTransaction.paymentUnitDate.year} ${refundRequest.paymentTransaction.paymentUnitDate.hour}: <fmt:formatNumber
            value="${refundRequest.paymentTransaction.paymentUnitDate.minute}" pattern="00" /> <c:choose>
            <c:when test="${refundRequest.paymentTransaction.paymentUnitDate.hour >= 12}">pm</c:when>
            <c:otherwise>am</c:otherwise>
          </c:choose></li>
        <li><span>Method:</span> ${refundRequest.paymentTransaction.paymentMethod} ${refundRequest.paymentTransaction.paymentSource}</li>
      </ul>

      <div class="control-group">
        <form:label path="verified" cssClass="checkbox" onclick="verifyPaymentInformation()">
          <form:checkbox path="verified" />
          I have verified the user's information
        </form:label>
      </div>
    </div>

    <div class="control-group">
      <form:label path="code" cssClass="control-label required">Refund Reason</form:label>
      <div class="controls">
        <form:select path="code" items="${refundCodes}" itemLabel="description" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="notes" cssClass="control-label required">Notes</form:label>
      <div class="controls">
        <form:textarea path="notes" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="jcaptcha" cssClass="control-label required">Security Verification</form:label>
      <div class="controls">
        <form:input path="jcaptcha" autocomplete="off" placeholder="Enter the below text" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
      <div class="controls">
        <div class="span5 captcha">
          <img id="jCaptchaImage" src="<spring:url value='/static/images/jcaptcha.jpg' htmlEscape='true' />" alt="Security image" />
        </div>
      </div>
      <div class="controls">
        <a href="#" class="span5 captchaReload" style="margin-left: 0;" tabindex="-1">request another image</a>
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
    </div>

  </fieldset>
</form:form>

<script type="text/javascript" src="<spring:url value='/static/javascript/jCaptcha.js' />"></script>

<script type="text/javascript">
	$(function() {
		$("#verified1").click(function() {
			$("#verifyInfo").toggleClass("alert-info");
			$("#verifyInfo").toggleClass("alert-success");
		});
	});

	function verifyPaymentInformation() {
		$("#verified1").click();
	}
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>