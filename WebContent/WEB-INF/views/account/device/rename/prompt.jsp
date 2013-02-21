<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <form:form id="renameDevice" cssClass="validatedForm" method="post" commandName="accountDetail.deviceInfo">

    <h3>Rename Your Device</h3>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="label" />
          <spring:bind path="accountDetail.deviceInfo">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <div class="row clearfix">Enter a new name for your device</div>
    <div class="row clearfix">
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="label" />
    </div>

    <div class="buttons">
      <a href="<spring:url value="/devices" />" class="mBtn">Cancel </a> <input type="submit" value="Rename"></input>
    </div>

  </form:form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>