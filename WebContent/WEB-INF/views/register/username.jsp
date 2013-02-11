<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <form:form id="registration_username" cssClass="validatedForm" method="post" commandName="simpleRegistrationLogin">

    <h3>Sign up for an account</h3>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.simpleRegistrationLogin'].allErrors}">
      <div class="row">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="email.value" />
          <form:errors path="email.confirmValue" />
          <form:errors path="password.value" />
          <form:errors path="password.confirmValue" />
          <!-- Global Errors -->
          <spring:bind path="simpleRegistrationLogin">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <p>Your email address will be your login for your account.</p>
    <p>Choose a password of at least 5 characters containing letters and numbers.</p>

    <!-- Email -->
    <div class="row">
      <form:label path="email.value" cssClass="required">
        <spring:message code="label.email" />
      </form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="email.value" />
      <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Not a valid email</span>
      </span>
    </div>
    <div class="row">
      <form:label path="email.confirmValue" cssClass="required">
        <spring:message code="label.confirmEmail" />
      </form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="email.confirmValue" />
      <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Emails do not match</span>
      </span>
    </div>

    <!-- Password -->
    <div class="row">
      <form:label path="password.value" cssClass="required">
        <spring:message code="label.password" />
      </form:label>
      <form:password cssClass="span-8" cssErrorClass="span-8 validationFailed" path="password.value" />
      <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Must have both numbers &amp;
          letters</span>
      </span>
    </div>
    <div class="row">
      <form:label path="password.confirmValue" cssClass="required">
        <spring:message code="label.confirmPassword" />
      </form:label>
      <form:password cssClass="span-8" cssErrorClass="span-8 validationFailed" path="password.confirmValue" />
      <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Passwords do not match</span>
      </span>
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <input id="registration_username_submit" type="submit" name="_eventId_submit" value="Continue"></input>
    </div>
  </form:form>

</div>

<script type="text/javascript">
	$(function() {
		$("#email\\.value").enableValidation(validateEmail).enableConfirmField();
		$("#password\\.value").enableValidation(validatePassword).enableConfirmField();
	});
</script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>