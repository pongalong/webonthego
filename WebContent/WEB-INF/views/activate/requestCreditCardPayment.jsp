<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <form:form id="payment_form" cssClass="validatedForm" method="post" commandName="creditCardPayment">

    <h3>Enter Your Payment Information</h3>

    <!-- Begin Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.creditCardPayment'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
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
      </div>
    </c:if>

    <p>Add your preferred payment information below. You can log back in to update this information at any time.</p>
    <p>Your credit card will be charged $10 today, which will give you a $10 credit in your Web on the Go account. Your monthly service fee and any usage
      (at 3.5¢ per MB) will be deducted from this $10 balance.</p>
    <p>You will be enrolled in automatic top-ups with this credit card. Each time your account balance falls below $2, your credit card will be charged $10.
      You may change your top-up amount in your online account options.</p>

    <!-- Billing Address -->
    <div class="clear"></div>
    <h3>Billing Address</h3>
    <div>

      <!-- Create hidden fields for all the user's addresses and display them as an option-->
      <c:if test="${not empty addresses}">
        <div id="addressList" class="row clearfix">
          <c:forEach items="${addresses}" var="address" varStatus="status">
            <input type="hidden" id="${status.index}_address1" value="${address.address1}" />
            <input type="hidden" id="${status.index}_address2" value="${address.address2}" />
            <input type="hidden" id="${status.index}_city" value="${address.city}" />
            <input type="hidden" id="${status.index}_state" value="${address.state}" />
            <input type="hidden" id="${status.index}_zip" value="${address.zip}" />
            <div class="address">
              <c:if test="${!empty address.address1}">
                <div>${address.address1}</div>
              </c:if>
              <c:if test="${!empty address.zip}">
                <div>${address.city}, ${address.state} ${address.zip}</div>
              </c:if>
              <div class="addressButtons">
                <a href="#" class="button semi-s addressSelect" name="${status.index}">Use This Address </a>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:if>

      <div id="billingAddress" style="margin-top: 12px; padding-top: 12px;">

        <div class="row clearfix">
          <form:label path="creditCard.address1" cssClass="required">Address 1</form:label>
          <form:input path="creditCard.address1" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>

        <div class="row clearfix">
          <form:label path="creditCard.address2">Address 2</form:label>
          <form:input path="creditCard.address2" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
        </div>

        <div class="row clearfix">
          <form:label path="creditCard.city" cssClass="required">City</form:label>
          <form:input path="creditCard.city" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>

        <div class="row clearfix">
          <form:label path="creditCard.state" cssClass="required">State</form:label>
          <form:select cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;" path="creditCard.state">
            <form:option value="0">
              <spring:message code="label.selectOne" />
            </form:option>
            <c:forEach var="state" items="${states}">
              <form:option value="${state.value}">${state.key}</form:option>
            </c:forEach>
          </form:select>
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>

        <div class="row clearfix">
          <form:label path="creditCard.zip" cssClass="required">Billing Zip Code</form:label>
          <form:input path="creditCard.zip" maxLength="5" cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
        </div>

        <div class="row clearfix">
          <form:label path="phoneNumber" cssClass="required">Phone Number</form:label>
          <form:input path="phoneNumber" maxLength="10" cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" />
          <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Digits Only Please</span></span>
        </div>

      </div>
    </div>

    <h3>Credit Card</h3>
    <div>
      <div class="row clearfix hidden">
        <form:label path="creditCard.isDefault" cssClass="required">Default</form:label>
        <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="creditCard.isDefault" value="Y" />
      </div>

      <div class="row clearfix">
        <form:label path="creditCard.nameOnCreditCard" cssClass="required">Name on Card</form:label>
        <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="creditCard.nameOnCreditCard" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
      </div>

      <div class="row clearfix">
        <form:label path="creditCard.creditCardNumber" cssClass="required">
          <spring:message code="label.payment.cardNumber" />
        </form:label>
        <form:input cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" maxLength="16" path="creditCard.creditCardNumber" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
      </div>

      <div class="row clearfix">
        <form:label path="creditCard.verificationcode" cssClass="required">Security Code</form:label>
        <form:input cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" maxLength="4" path="creditCard.verificationcode"
          cssStyle="width:60px;" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span> <a id="cvvInfo" href="#"
          style="margin-left: 10px;" tabindex="-1">What is this?</a> <span class="hover_tooltip">
          <p>This is the 3 digit code on the back of the card for Visa and Mastercard, or the 4 digit number on the front for American Express.</p>
          <p>
            <img src="<spring:url value='/static/images/creditCard/securityExample.png' />" />
          </p>
        </span>
      </div>

      <div class="row clearfix">
        <form:label path="creditCard.expirationDate" cssClass="required">Expiration Date</form:label>
        <select id="monthSelect" style="width: 50px; margin-right: 10px;">
          <c:forEach var="month" items="${months}">
            <option value="${month.key}">${month.key}</option>
          </c:forEach>
        </select> <select id="yearSelect" style="width: 70px;">
          <c:forEach var="year" items="${years}">
            <option value="${year.value}">${year.key}</option>
          </c:forEach>
        </select>
        <form:input cssStyle="display: none;" cssClass="numOnly" maxLength="4" cssErrorClass="numOnly verificationFailed" path="creditCard.expirationDate" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject"></span></span>
      </div>

      <div id="creditCardImages" class="row clearfix pushed">
        <img id="ImgAmex" src="<spring:url value='/static/images/creditCard/iconAmex.png' />" /> 
        <img id="ImgMastercard" src="<spring:url value='/static/images/creditCard/iconMasterCard.png' />" /> 
        <img id="ImgVisa" src="<spring:url value='/static/images/creditCard/iconVisa.png' />" /> 
        <img id="ImgDiscover" src="<spring:url value='/static/images/creditCard/iconDiscover.png' />" />
      </div>
    </div>

    <!-- Coupon -->
    <h3 style="margin: 10px 0 10px 0; padding: 10px 0 0 0; border-top: 1px #ccc solid;" onClick="$(this).next('div').slideToggle();">
      <img src="<spring:url value='/static/images/buttons/icons/add.png' />" style="vertical-align: middle;" /> Click Here If You Have a Coupon
    </h3>

    <div style="display: none;">
      <div class="row clearfix">
        <form:label cssClass="required" path="coupon.couponCode">Enter Coupon Code</form:label>
        <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="coupon.couponCode" />
      </div>
      <div class="row clearfix pushed">
        <span id="couponMessage"></span>
      </div>
    </div>

    <div class="clear"></div>
    <!-- Buttons -->
    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue" />
    </div>

  </form:form>

</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>