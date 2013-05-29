<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form method="post" commandName="creditCard" cssClass="form-horizontal">

  <fieldset>
    <legend>Credit Card Information</legend>

    <!-- Begin Errors -->
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
        <!-- Global Errors -->
        <spring:bind path="creditCard">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>
    <!-- End Errors -->

    <div class="controls">
      <form:label path="isDefault" cssClass="checkbox">
        <form:checkbox path="isDefault" value="Y" />Default</form:label>
    </div>

    <div class="control-group">
      <form:label path="nameOnCreditCard" cssClass="control-label required">Name on Card</form:label>
      <div class="controls">
        <form:input path="nameOnCreditCard" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="creditCardNumber" cssClass="control-label required">
        <spring:message code="label.payment.cardNumber" />
      </form:label>
      <div class="controls">
        <form:input path="creditCardNumber" cssClass="span5 numOnly" cssErrorClass="span5 validationFailed numOnly" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="verificationcode" cssClass="control-label required">Security Code</form:label>
      <div class="controls">
        <form:input cssClass="span1 numOnly" cssErrorClass="span1 numOnly validationFailed" maxLength="4" path="verificationcode" />
        <a id="cvvInfo" href="#" style="margin-left: 10px;">What is this?</a> 
        <span class="hover_tooltip">
          This is the 3 digit code on the back of the card for Visa and Mastercard, or the 4 digit number on the front for American Express.<br /> 
          <img src="<spring:url value='/static/images/creditCard/securityExample.png' />" />
        </span>
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
        <form:input cssStyle="display:none;" cssClass="numOnly" maxLength="4" cssErrorClass="numOnly verificationFailed" path="expirationDate" />
      </div>
    </div>


    <div id="creditCardImages" class="controls">
      <img id="ImgAmex" src="<spring:url value='/static/images/creditCard/iconAmex.png' />" /> <img id="ImgMastercard"
        src="<spring:url value='/static/images/creditCard/iconMasterCard.png' />" /> <img id="ImgVisa"
        src="<spring:url value='/static/images/creditCard/iconVisa.png' />" /> <img id="ImgDiscover"
        src="<spring:url value='/static/images/creditCard/iconDiscover.png' />" />
    </div>

    <h3>Billing Address</h3>

    <div class="control-group">
      <form:label path="address1" cssClass="control-label required">Address 1</form:label>
      <div class="controls">
        <form:input path="address1" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="address2" cssClass="control-label required">Address 2</form:label>
      <div class="controls">
        <form:input path="address2" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="city" cssClass="control-label required">City</form:label>
      <div class="controls">
        <form:input path="city" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="state" cssClass="control-label required">State</form:label>
      <div class="controls">
        <form:select path="state" cssClass="span5" cssErrorClass="span5 validationFailed">
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
        <form:input path="zip" maxLength="5" cssClass="span5 numOnly" cssErrorClass="span5 numOnly validationFailed" />
      </div>
    </div>


    <!-- Buttons -->
    <div class="controls">
      <button onclick="location.href='<spring:url value="/profile" />'" class="button">Cancel</button>
      <button type="submit" name="_eventId_submitAddCreditCard">Add</button>
    </div>

  </fieldset>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>