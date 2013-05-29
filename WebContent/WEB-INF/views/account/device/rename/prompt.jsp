<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="accountDetail.deviceInfo" method="post">
  <fieldset>
    <legend>Rename Your Device</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="label" />
        <spring:bind path="accountDetail.deviceInfo">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <form:label path="label">Enter a new name for your device</form:label>
    <form:input path="label" cssClass="span5" cssErrorClass="span5 validationFailed" />

    <div>
      <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
      <button type="submit" class="button">Rename</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>