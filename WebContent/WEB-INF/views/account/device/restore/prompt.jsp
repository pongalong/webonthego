<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form method="POST" commandName="accountDetail">
  <fieldset>
    <legend>Restore Device</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="deviceInfo.id" />
        <form:errors path="deviceInfo.value" />
        <form:errors path="deviceInfo.label" />
        <spring:bind path="accountDetail">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="alert alert-info">
      <h4>Attention!</h4>
      <p>You are about to restore a device and the associated account.</p>
    </div>

    <p>Are you sure you want to restore device ${accountDetail.deviceInfo.label}?</p>

    <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
    <button type="submit" class="button">Restore</button>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>