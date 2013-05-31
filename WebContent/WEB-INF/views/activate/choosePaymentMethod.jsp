<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="creditCardPayment" method="post" cssClass="form-horizontal">
  <fieldset>
    <legend> Choose a Payment Method </legend>

    <!-- Error Alert -->
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
      <p>The payment method you choose will be used to keep your device topped-up.</p>
    </div>

    <div class="control-group">
      <form:label path="creditCard.paymentid" cssClass="control-label">Existing Methods</form:label>
      <div class="controls">
        <form:select path="creditCard.paymentid" cssClass="span6">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <c:forEach var="cc" items="${existingPaymentMethods}">
            <form:option value="${cc.paymentid}">
              ${cc.creditCardNumber} (${cc.nameOnCreditCard})
              <c:if test="${cc.isDefault == 'Y'}">Default</c:if>
            </form:option>
          </c:forEach>
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="coupon.couponCode" cssClass="control-label">Coupon Code</form:label>
      <div class="controls">
        <form:input path="coupon.couponCode" placeholder="Enter a Code If You Have One" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span id="couponMessage"></span>
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button" name="_eventId_new">Add New Payment Method</button>
      <button type="submit" class="button" name="_eventId_select">Continue</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>