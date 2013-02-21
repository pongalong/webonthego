<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-15">
  <form:form id="resetPassword" cssClass="validatedForm" method="POST" commandName="verifyIdentity">

    <h3>Enter Your Registered E-Mail</h3>
    <p style="margin-bottom: 10px;">To reset your password please enter your email. We will send you further instructions to complete the request.</p>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="email" />
          <spring:bind path="verifyIdentity">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <div class="row clearfix">
      <form:label path="email" cssClass="required">E-Mail Address</form:label>
    </div>
    <div class="row clearfix">
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="email" />
    </div>

    <input type="submit" value="Reset My Password!"></input>

  </form:form>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>