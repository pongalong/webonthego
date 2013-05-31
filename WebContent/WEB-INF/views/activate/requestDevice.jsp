<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="device" method="post" cssClass="form-horizontal">

  <fieldset>
    <Legend>Enter Your Device Information</Legend>

    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.device'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="value" />
        <form:errors path="label" />
        <spring:bind path="device">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="well">
      <p>To activate your device, enter your serial number (ESN) and assign a nickname (example: "Sam's Mifi"). Your serial number is an 11- or 16-digit
        number printed on a label that's either on the bottom of your device or behind the battery.</p>
      <p>You may see an ESN-HEX code (letters and numbers) or ESN-DEC code (only numbers) near a barcode on the label. Either number will work to activate
        your device, but the DEC code may be easier to identify.</p>
    </div>

    <!-- Device Esn -->
    <div class="control-group">
      <form:label path="value" cssClass="control-label required">Serial Number (ESN)</form:label>
      <div class="controls">
        <form:input path="value" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <!-- Device Label -->
    <div class="control-group">
      <form:label path="label" cssClass="control-label required">Nickname</form:label>
      <div class="controls">
        <form:input path="label" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <!-- Buttons -->
    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>