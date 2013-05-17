<%@ include file="/WEB-INF/views/include/header.jsp"%>

<h3>Enter Your Registered E-Mail</h3>

<form:form id="resetPassword" cssClass="validatedForm" cssStyle="padding-bottom: 0;" method="POST" commandName="verifyIdentity">

  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
    <div class="alert error">
      <h1>Please correct the following problems</h1>
      <form:errors path="email" />
      <spring:bind path="verifyIdentity">
        <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
          <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
        </c:forEach>
      </spring:bind>
    </div>
  </c:if>

  <div class="alert info" style="margin-bottom: 0px;">

    <p>To reset your password please enter your email. We will send you further instructions to complete the request.</p>

    <div class="row clearfix">
      <form:input path="email" placeholder="Email Address" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <p>
      <input type="submit" value="Reset My Password" />
    </p>

  </div>
</form:form>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>