<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form:form id="reconnect" cssClass="validatedForm" method="POST" commandName="accountDetail">
    <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Reconnect Device</h3>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="deviceInfo.id" />
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

    <div class="notice">
      <h4>Attention!</h4>
      <p>You are about to Reconnect a device and the associated account.</p>
    </div>

    <p>Are you sure you want to reactivate device ${deviceInfo.label}?</p>

    <div class="row clearfix" style="display: none;">
      <form:input path="deviceInfo.id" />
      <form:input path="deviceInfo.label" />
      <form:input path="deviceInfo.value" />
    </div>

    <div class="buttons">
      <a class="mBtn" href="<spring:url value="/devices" />">Cancel</a> <input type="submit" value="Reactivate"></input>
    </div>
  </form:form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>