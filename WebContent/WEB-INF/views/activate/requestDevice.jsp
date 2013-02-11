<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <form:form id="device_form" cssClass="validatedForm" method="post" commandName="device">
    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.device'].allErrors}">
      <div class="row">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="value" />
          <form:errors path="label" />
          <spring:bind path="device">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <h3>Enter Your Device Information</h3>

    <p>To activate your device, enter your serial number (ESN) and assign a nickname (example: "Sam's Mifi"). Your serial number is an 11- or 16-digit
      number printed on a label that's either on the bottom of your device or behind the battery.</p>
    <p>You may see an ESN-HEX code (letters and numbers) or ESN-DEC code (only numbers) near a barcode on the label. Either number will work to activate
      your device, but the DEC code may be easier to identify.</p>

    <!-- Device Esn -->
    <div class="row">
      <form:label cssClass="required" path="value">Serial Number (ESN)</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="value" />
    </div>

    <!-- Device Label -->
    <div class="row">
      <form:label cssClass="required" path="label">Nickname</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="label" />
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue" />
    </div>
  </form:form>

</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>