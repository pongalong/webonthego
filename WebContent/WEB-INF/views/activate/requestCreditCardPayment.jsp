<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="creditCardPayment" method="post" cssClass="form-horizontal">

  <fieldset>
    <legend>Enter Your Payment Information</legend>

    <!-- Begin Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.creditCardPayment'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="creditCard.nameOnCreditCard" />
        <form:errors path="creditCard.creditCardNumber" />
        <form:errors path="creditCard.verificationcode" />
        <form:errors path="creditCard.expirationDate" />
        <form:errors path="creditCard.address1" />
        <form:errors path="creditCard.address2" />
        <form:errors path="creditCard.city" />
        <form:errors path="creditCard.state" />
        <form:errors path="creditCard.zip" />
        <form:errors path="coupon.couponCode" />
        <form:errors path="coupon.startDate" />
        <form:errors path="coupon.endDate" />
        <form:errors path="coupon.quantity" />
        <spring:bind path="creditCardPayment">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="well">
      <p>Add your preferred payment information below. You can log back in to update this information at any time.</p>
      <p>Your credit card will be charged $10 today, which will give you a $10 credit in your Web on the Go account. Your monthly service fee and any usage
        (at 3.5¢ per MB) will be deducted from this $10 balance.</p>
      <p>You will be enrolled in automatic top-ups with this credit card. Each time your account balance falls below $2, your credit card will be charged
        $10. You may change your top-up amount in your online account options.</p>
    </div>

    <fieldset>
      <legend>Credit Card</legend>

      <div class="control-group">
        <form:label path="creditCard.nameOnCreditCard" cssClass="control-label required">Name on Card</form:label>
        <div class="controls">
          <form:input path="creditCard.nameOnCreditCard" cssClass="span6" cssErrorClass="span6 validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.creditCardNumber" cssClass="control-label required">
          <spring:message code="label.payment.cardNumber" />
        </form:label>
        <div class="controls">
          <form:input path="creditCard.creditCardNumber" cssClass="span5 numOnly" cssErrorClass="span5 numOnly validationFailed" maxLength="16" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
          <div id="creditCardImages" style="margin-top: 5px;">
            <img id="ImgAmex" src="<spring:url value='/static/images/creditCard/iconAmex.png' />" /> <img id="ImgMastercard"
              src="<spring:url value='/static/images/creditCard/iconMasterCard.png' />" /> <img id="ImgVisa"
              src="<spring:url value='/static/images/creditCard/iconVisa.png' />" /> <img id="ImgDiscover"
              src="<spring:url value='/static/images/creditCard/iconDiscover.png' />" />
          </div>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.verificationcode" cssClass="control-label required">CVV Number</form:label>
        <div class="controls">
          <form:input path="creditCard.verificationcode" cssClass="span1 numOnly" cssErrorClass="span1 numOnly verificationFailed" maxLength="4" />
          <a id="cvvInfo" href="#" class="btn" data-toggle="popover" data-placement="right" data-original-title="CVV Code" tabIndex="-1">What is this?</a>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.expirationDate" cssClass="control-label required">Expiration Date</form:label>
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
          <form:input path="creditCard.expirationDate" cssClass="numOnly hidden" maxLength="4" cssErrorClass="numOnly hidden verificationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

    </fieldset>

    <!-- Billing Address -->
    <fieldset>
      <legend>Billing Address</legend>

      <div class="control-group">
        <form:label path="creditCard.address1" cssClass="control-label required">Address 1</form:label>
        <div class="controls">
          <form:input path="creditCard.address1" cssClass="span6" cssErrorClass="span6 validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.address2" cssClass="control-label">Address 2</form:label>
        <div class="controls">
          <form:input path="creditCard.address2" cssClass="span6" cssErrorClass="span6 validationFailed" />
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.city" cssClass="control-label required">City</form:label>
        <div class="controls">
          <form:input path="creditCard.city" cssClass="span6" cssErrorClass="span6 validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.state" cssClass="control-label required">State</form:label>
        <div class="controls">
          <form:select path="creditCard.state" cssClass="span6" cssErrorClass="span6 validationFailed">
            <form:option value="0">
              <spring:message code="label.selectOne" />
            </form:option>
            <c:forEach var="state" items="${states}">
              <form:option value="${state.value}">${state.key}</form:option>
            </c:forEach>
          </form:select>
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

      <div class="control-group">
        <form:label path="creditCard.zip" cssClass="control-label required">Billing Zip Code</form:label>
        <div class="controls">
          <form:input path="creditCard.zip" maxLength="5" cssClass="span6 numOnly" cssErrorClass="span6 numOnly validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>
      </div>

      <div class="control-group">
        <form:label path="phoneNumber" cssClass="control-label required">Phone Number</form:label>
        <div class="controls">
          <form:input path="phoneNumber" maxLength="10" cssClass="span6 numOnly" cssErrorClass="span6 numOnly validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Digits Only Please</span></span>
        </div>
      </div>

    </fieldset>

    <!-- Coupon -->
    <fieldset>
      <legend>Promotions</legend>

      <div class="control-group">
        <form:label path="coupon.couponCode" cssClass="control-label">Coupon Code</form:label>
        <div class="controls">
          <form:input path="coupon.couponCode" placeholder="Enter a Code If You Have One" cssClass="span6" cssErrorClass="span6 validationFailed" />
          <span id="couponMessage"></span>
        </div>
      </div>

    </fieldset>

    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
    </div>

  </fieldset>
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

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>