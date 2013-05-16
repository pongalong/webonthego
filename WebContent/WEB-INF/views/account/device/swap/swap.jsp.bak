<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form:form id="swap" cssClass="validatedForm" method="post" commandName="accountDetail">

    <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Serial Number</h3>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="deviceInfo.value" />
          <form:errors path="deviceInfo.label" />
          <spring:bind path="accountDetail">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <p>To swap your device we need to know the new serial number.</p>

    <!-- Esn -->
    <div class="row clearfix">
      <form:label cssClass="required" path="deviceInfo.value">Serial Number (ESN)</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="deviceInfo.value" />
    </div>

    <div class="row clearfix">
      <form:label cssClass="required" path="deviceInfo.label">Descriptive Name</form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="deviceInfo.label" />
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