<%@ include file="/WEB-INF/views/include/header.jsp"%>

<h3>You Have Requested To Reset Your Password</h3>


<form:form id="resetPassword" cssClass="validatedForm" cssStyle="padding-bottom: 0;" method="POST" commandName="verifyIdentity">

  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
    <div class="alert error">
      <h1>Please correct the following problems</h1>
      <spring:bind path="verifyIdentity">
        <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
          <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
        </c:forEach>
      </spring:bind>
    </div>
  </c:if>

  <div class="alert info">

    <p>Answer your security question that you supplied at registration.</p>

    <p style="font-weight: bold;">${securityQuestion.question}</p>

    <div class="row clearfix">
      <form:input path="hintAnswer" />
    </div>

    <p>
      <input type="submit" value="Reset My Password"></input>
    </p>

  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>