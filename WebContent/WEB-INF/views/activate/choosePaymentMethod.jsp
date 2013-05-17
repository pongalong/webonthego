<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <h3>Choose a Payment Method</h3>

  <form:form id="choose_payment" cssClass="validatedForm" method="post" commandName="creditCardPayment">
    <!-- Error Alert -->
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

    <p>The payment method you choose will be used to keep your device topped-up.</p>

    <!-- List Payment Methods -->
    <c:forEach var="creditCard" items="${existingPaymentMethods}" varStatus="status">
      <div id="${creditCard.paymentid}" class="objectSelect yellowSelect unselected">
        <c:if test="${creditCard.isDefault == 'Y'}">
          <span style="font-weight: bold; float: right;">Default Method</span>
        </c:if>
        <p style="margin: 0; padding: 0;">${creditCard.nameOnCreditCard}</p>
        <p style="margin: 0; padding: 0;">${creditCard.creditCardNumber}</p>
      </div>
    </c:forEach>

    <!-- Payment Method Buttons -->
    <a id="addNewMethodButton" href="#" class="button escape-m" style="float: right;"><span>Add New Card</span> </a>
    <a id="resetChoiceButton" href="#" class="button escape-m" style="display: none; float: right;"><span>Choose Another Card</span> </a>
    <div class="clear"></div>

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

    <form:input path="creditCard.paymentid" cssClass="hidden" />

    <div class="buttons">
      <input id="addNewMethod_submit" type="submit" name="_eventId_new" value="Add New Payment Method" /> <input type="submit" name="_eventId_select"
        value="Continue" />
    </div>


  </form:form>

</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/pages/selectPaymentMethod.js' />"></script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>