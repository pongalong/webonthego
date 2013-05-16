<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form:form id="refund" cssClass="validatedForm" method="POST" commandName="refundRequest">

    <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Reverse Transaction</h3>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.refundRequest'].allErrors}">
      <div class="alert error">
        <h1>Please correct the following problems</h1>
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

    <div id="verifyInfo" class="info">
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

      <div class="row clearfix">
        <form:checkbox path="verified" cssErrorClass="validationFailed" cssStyle="vertical-align: middle;" />
        <span style="vertical-align: middle;" onclick="verifyPaymentInformation()">I have verified the user's information </span>
      </div>
    </div>

    <div class="row clearfix">
      <form:label path="code" cssClass="required">Refund Reason</form:label>
      <form:select path="code" items="${refundCodes}" itemLabel="description" cssErrorClass="validationFailed" />
    </div>

    <div class="row clearfix">
      <form:label path="notes" cssClass="required">Notes</form:label>
      <form:textarea path="notes" cssErrorClass="validationFailed" />
    </div>

    <div class="clear"></div>

    <!-- Jcaptcha -->
    <div class="row clearfix captchaImage">
      <form:label path="jcaptcha" cssClass="required">Word Verification</form:label>
      <div class="image">
        Enter the text in the image below<img id="jCaptchaImage" src="<spring:url value='/static/images/jcaptcha.jpg' htmlEscape='true' />" alt="Security image" />
      </div>
      <a href="#" class="pushed captchaReload" tabindex="-1">request another image</a>
    </div>
    <div class="clear"></div>
    <div class="row clearfix pushed">
      <form:input cssErrorClass="validationFailed" autocomplete="off" path="jcaptcha" />
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <input id="refundSubmit" type="submit" name="_eventId_submit" value="Continue" />
    </div>
  </form:form>

</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/jCaptcha.js' />"></script>

<script type="text/javascript">
	$(function() {
		$("#verified1").click(function() {
			$("#verifyInfo").toggleClass("info");
			$("#verifyInfo").toggleClass("success");
		});
	});

	function verifyPaymentInformation() {
		$("#verified1").click();
	}
</script>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>