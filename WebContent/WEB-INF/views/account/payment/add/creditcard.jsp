<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="creditCard" method="post" cssClass="form-horizontal">

  <fieldset>
    <legend>Card Information</legend>

    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.creditCard'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="nameOnCreditCard" />
        <form:errors path="creditCardNumber" />
        <form:errors path="verificationcode" />
        <form:errors path="expirationDate" />
        <form:errors path="address1" />
        <form:errors path="address2" />
        <form:errors path="city" />
        <form:errors path="state" />
        <form:errors path="zip" />
        <spring:bind path="creditCard">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="control-group">
      <form:label path="isDefault" class="control-label">Set As Default</form:label>
      <div class="controls">
        <form:checkbox path="isDefault" value="Y" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="nameOnCreditCard" cssClass="control-label required">Name on Card</form:label>
      <div class="controls">
        <form:input path="nameOnCreditCard" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="creditCardNumber" cssClass="control-label required">
        <spring:message code="label.payment.cardNumber" />
      </form:label>
      <div class="controls">
        <form:input path="creditCardNumber" cssClass="span6 numOnly" cssErrorClass="span6 numOnly validationFailed" maxLength="16" />
        <div id="creditCardImages" style="margin-top: 5px;">
          <img id="ImgAmex" src="<spring:url value='/static/images/creditCard/iconAmex.png' />" /> <img id="ImgMastercard"
            src="<spring:url value='/static/images/creditCard/iconMasterCard.png' />" /> <img id="ImgVisa"
            src="<spring:url value='/static/images/creditCard/iconVisa.png' />" /> <img id="ImgDiscover"
            src="<spring:url value='/static/images/creditCard/iconDiscover.png' />" />
        </div>
      </div>
    </div>

    <div class="control-group">
      <form:label path="verificationcode" cssClass="control-label required">CVV Number</form:label>
      <div class="controls">
        <form:input path="verificationcode" cssClass="span1 numOnly" cssErrorClass="span1 numOnly verificationFailed" maxLength="4" />
        <a id="cvvInfo" href="#" class="btn" data-toggle="popover" data-placement="right" data-original-title="CVV Code">What is this?</a>
      </div>
    </div>

    <div class="control-group">
      <form:label path="expirationDate" cssClass="control-label required">Expiration Date</form:label>
      <div class="controls">
        <select id="monthSelect" class="span1">
          <c:forEach var="month" items="${months}">
            <option value="${month.key}">${month.key}</option>
          </c:forEach>
        </select> <select id="yearSelect" class="span2">
          <c:forEach var="year" items="${years}">
            <option value="${year.value}">${year.key}</option>
          </c:forEach>
        </select>
        <form:input path="expirationDate" cssClass="numOnly hidden" maxLength="4" cssErrorClass="numOnly hidden verificationFailed" />
      </div>
    </div>

  </fieldset>

  <fieldset>
    <legend>Billing Address</legend>

    <div class="control-group">
      <form:label path="address1" cssClass="control-label required">Address 1</form:label>
      <div class="controls">
        <form:input path="address1" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="address2" cssClass="control-label">Address 2</form:label>
      <div class="controls">
        <form:input path="address2" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="city" cssClass="control-label required">City</form:label>
      <div class="controls">
        <form:input path="city" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="state" cssClass="control-label required">State</form:label>
      <div class="controls">
        <form:select path="state" cssClass="span6" cssErrorClass="span6 validationFailed">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <form:options items="${states}" itemValue="value" itemLabel="key" />
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="zip" cssClass="control-label required">Billing Zip Code</form:label>
      <div class="controls">
        <form:input path="zip" maxLength="5" cssClass="span6 numOnly" cssErrorClass="span6 numOnly validationFailed" />
      </div>
    </div>

  </fieldset>

  <!-- Buttons -->
  <div class="controls">
    <button type="button" class="button" onclick="location.href='<spring:url value="/profile" />'">Cancel</button>
    <button type="submit" class="button">Save</button>
  </div>

</form:form>

<script>
	$(function() {
		var txt = "<p>This is the 3 digit code on the back of the card for Visa and Mastercard, or the 4 digit number on the front for American Express.</p>";
		var img = "<img src='/static/images/creditCard/securityExample.png' />";
		$("#cvvInfo").popover({
			content : txt + img,
			html : true
		});
		$("#cvvInfo").click(function(e) {
			e.preventDefault();
		});
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>