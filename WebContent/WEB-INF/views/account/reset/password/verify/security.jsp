<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div style="width: 400px;">
  <form:form id="resetPassword" cssClass="validatedForm" method="POST" commandName="verifyIdentity">

    <h3>You Have Requested To Reset Your Password</h3>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.verifyIdentity'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <spring:bind path="verifyIdentity">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <p>
      Answer your security question that you supplied at registration.<br /> <span style="font-weight: bold;">${question}</span>
    </p>

    <div class="row clearfix">
      <form:input cssStyle="width: 100%;" path="hintAnswer" />
    </div>

    <div class="buttons">
      <input type="submit" value="Continue"></input>
    </div>

  </form:form>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>