<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<div class="span12">
  <form:form id="resetPassword" method="POST" commandName="verifyIdentity" cssClass="form-horizontal">
    <fieldset>
      <legend>Enter Your Registered E-Mail</legend>

      <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <h4>Please correct the following problems</h4>
          <form:errors path="email" />
          <spring:bind path="verifyIdentity">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </c:if>

      <p>To reset your password please enter your email. We will send you further instructions to complete the request.</p>

      <div class="control-group">
        <form:label path="email" cssClass="control-label required">Email</form:label>
        <div class="controls">
          <form:input path="email" placeholder="Email Address" cssClass="span6" cssErrorClass="span6 validationFailed" />
        </div>
      </div>

      <div class="controls">
        <button type="submit" class="button">Reset My Password</button>
      </div>

    </fieldset>
  </form:form>
</div>

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>