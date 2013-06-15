<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="newDevice" method="post" class="form-horizontal">

  <fieldset>
    <legend>Swap Device</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.newDevice'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="value" />
        <form:errors path="label" />
        <spring:bind path="newDevice">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="alert alert-info">

      <p>Swapping will transfer your service to the new device. Your old device will no longer be in use. To swap your device we need to know the new serial
        number.</p>

      <p>
        <span style="color: red">*</span> the target device must not be in use (available for activation)
      </p>

      <ul class="info">
        <li><span style="font-weight: bold;">Name:</span> ${accountDetail.deviceInfo.label}</li>
        <li><span style="font-weight: bold;">ESN:</span> ${accountDetail.deviceInfo.value}</li>
        <li><span style="font-weight: bold;">Balance:</span> ${accountDetail.account.balance}</li>
      </ul>

    </div>

    <!-- Esn -->
    <div class="control-group">
      <form:label path="value" cssClass="control-label required">New ESN</form:label>
      <div class="controls">
        <form:input path="value" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="label" cssClass="control-label required">New Name</form:label>
      <div class="controls">
        <form:input path="label" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <!-- Buttons -->
    <div class="controls">
      <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
      <button type="submit" class="button">Swap</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>