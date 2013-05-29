<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>
<h3>Remove Credit Card</h3>

<form:form name="deleteCreditCard" id="deleteCreditCard" method="post" commandName="creditCard">

  <!-- Error Alert -->
  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.creditCard'].allErrors}">
    <div class="alert error">
      <h1>Please correct the following problems</h1>
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

  <p>Are you sure you want to remove the following credit card?</p>
  <p style="margin-left: 20px; font-weight: bold;">
    Name: ${creditCard.nameOnCreditCard}<br /> Card: ${creditCard.creditCardNumber}<br /> Expiration: ${creditCard.expirationDate}<br />
  </p>

  <div class="hidden">
    <div class="row clearfix">
      <form:label path="isDefault">Default</form:label>
      <form:checkbox disabled="disabled" path="isDefault" value="Y" />
    </div>

    <div class="row clearfix" style="display: none;">
      <form:label path="alias">Alias</form:label>
      <form:input disabled="disabled" path="alias" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:label path="nameOnCreditCard" cssClass="required">Name on Card</form:label>
      <form:input disabled="disabled" path="nameOnCreditCard" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:label path="creditCardNumber" cssClass="required">
        <spring:message code="label.payment.cardNumber" />
      </form:label>
      <form:input disabled="disabled" path="creditCardNumber" cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" maxLength="16" />
    </div>

    <div class="row clearfix">
      <form:label path="verificationcode" cssClass="required">CVV Number</form:label>
      <form:input disabled="disabled" path="verificationcode" cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly verificationFailed" maxLength="4"
        cssStyle="width:60px;" />
      <a id="cvvInfo" href="#" style="margin-left: 10px;">What is this?</a> <span class="hover_tooltip">This is the 3 digit code on the back of the card
        for Visa and Mastercard, or the 4 digit number on the front for American Express.<br /> <img
        src="<spring:url value='/static/images/creditCard/securityExample.png' />" />
      </span>
    </div>

    <div class="row clearfix">
      <form:label path="expirationDate" cssClass="required">Expiration Date</form:label>
      <select id="monthSelect" style="width: 50px; margin-right: 10px;">
        <c:forEach var="month" items="${months}">
          <option value="${month.key}">${month.key}</option>
        </c:forEach>
      </select> <select id="yearSelect" style="width: 70px;">
        <c:forEach var="year" items="${years}">
          <option value="${year.value}">${year.key}</option>
        </c:forEach>
      </select>
      <form:input cssStyle="display:none;" cssClass="numOnly" maxLength="4" cssErrorClass="numOnly verificationFailed" path="expirationDate" />
    </div>

    <div id="creditCardImages" class="row clearfix pushed">
      <img id="ImgAmex" src="<spring:url value='/static/images/creditCard/iconAmex.png' />" /> <img id="ImgMastercard"
        src="<spring:url value='/static/images/creditCard/iconMasterCard.png' />" /> <img id="ImgVisa"
        src="<spring:url value='/static/images/creditCard/iconVisa.png' />" /> <img id="ImgDiscover"
        src="<spring:url value='/static/images/creditCard/iconDiscover.png' />" />
    </div>

    <h3>Edit Billing Address</h3>

    <div id="billingAddress" style="margin-top: 12px; padding-top: 12px;">
      <div class="row clearfix">
        <form:label path="address1" cssClass="required">Address 1</form:label>
        <form:input disabled="disabled" path="address1" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>
      <div class="row clearfix">
        <form:label path="address2">Address 2</form:label>
        <form:input disabled="disabled" path="address2" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>
      <div class="row clearfix">
        <form:label path="city" cssClass="required">City</form:label>
        <form:input disabled="disabled" path="city" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>

      <div class="row clearfix">
        <form:label path="state" cssClass="required">State</form:label>
        <form:select disabled="disabled" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;" path="state">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <c:forEach var="state" items="${states}">
            <form:option value="${state.value}">${state.key}</form:option>
          </c:forEach>
        </form:select>
      </div>

      <div class="row clearfix">
        <form:label path="zip" cssClass="required">Billing Zip Code</form:label>
        <form:input disabled="disabled" path="zip" maxLength="5" cssClass="span-8 numOnly" cssErrorClass="span-8 numOnly validationFailed" />
      </div>
    </div>

  </div>

  <!-- Buttons -->
  <div class="buttons">
    <a href="<spring:url value="/profile" />" class="mBtn">Cancel </a> <input type="submit" value="Remove" />
  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>