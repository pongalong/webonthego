<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">
  <h3>Terms and Conditions</h3>
  <form:form id="registration_terms" cssClass="validatedForm" method="post" commandName="simpleRegistrationTerms">

    <!-- Begin Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.simpleRegistrationTerms'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="acceptTerms" />
          <!-- Global Errors -->
          <spring:bind path="simpleRegistrationTerms">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <!-- Display Plans -->
    <div class="row clearfix">
      <div class="registrationTerms"><%@ include file="/WEB-INF/views/terms/registration_tos.jsp"%></div>
    </div>

    <!-- Term Acceptance -->
    <div class="row clearfix">
      <form:checkbox path="acceptTerms" cssErrorClass="validationFailed" cssStyle="vertical-align: middle;" />
      <span style="vertical-align: middle;" onclick="$('#acceptTerms1').click()">I have read and understand the terms and conditions.</span>
    </div>

    <div class="clear"></div>

    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue" />
    </div>

  </form:form>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>