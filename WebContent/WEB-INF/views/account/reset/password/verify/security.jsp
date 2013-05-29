<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<div class="span12">

  <form:form id="resetPassword" method="POST" commandName="verifyIdentity">

    <fieldset>
      <legend>You Have Requested To Reset Your Password</legend>

      <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <h4>Please correct the following problems</h4>
          <spring:bind path="verifyIdentity">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </c:if>

      <p>Answer your security question that you supplied at registration.</p>

      <form:label path="hintAnswer" cssClass="control-label">${securityQuestion.question}</form:label>
      <form:input path="hintAnswer" cssClass="span6" />
      <div>
        <button type="submit" class="button">Reset My Password</button>
      </div>

    </fieldset>
  </form:form>

</div>
<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>