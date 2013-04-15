<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form:form id="swap" cssClass="validatedForm" method="post" commandName="newDevice">

    <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Serial Number</h3>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.newDevice'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="value" />
          <form:errors path="label" />
          <spring:bind path="newDevice">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <p>Swapping will transfer your service to the new device. Your old device will no longer be in use. To swap your device we need to know the new serial
      number.</p>
      
    <p><span style="color: red">*</span> the target device must not be in use (available for activation)</p>

    <div class="row clearfix info">
      <h4 style="margin: 0; padding: 0;">Selected Device</h4>
      <ul style="list-style-type: none; margin: 5px 10px; padding: 0; font-size: .9em;">
        <li><span style="font-weight: bold;">Name:</span> ${accountDetail.deviceInfo.label}</li>
        <li><span style="font-weight: bold;">ESN:</span> ${accountDetail.deviceInfo.value}</li>
        <li><span style="font-weight: bold;">Balance:</span> ${accountDetail.account.balance}</li>
      </ul>
    </div>

    <!-- Esn -->
    <div class="row clearfix">
      <form:label cssClass="required" path="value">New Serial Number (ESN)</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="value" />
    </div>

    <div class="row clearfix">
      <form:label cssClass="required" path="label">New Descriptive Name</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="label" />
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <a href="<spring:url value="/devices" />" class="mBtn">Cancel </a> <input type="submit" value="Continue" />
    </div>
  </form:form>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>