<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="registrationLogin" method="post" cssClass="form-horizontal">

  <fieldset>
    <legend>Sign Up For An Account</legend>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.registrationLogin'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="email.value" />
        <form:errors path="email.confirmValue" />
        <form:errors path="password.value" />
        <form:errors path="password.confirmValue" />
        <!-- Global Errors -->
        <spring:bind path="registrationLogin">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="well">
      <p>Your email address will be your login for your account.</p>
      <p>Choose a password of at least 5 characters containing letters and numbers.</p>
    </div>

    <!-- Email -->
    <div class="control-group">
      <form:label path="email.value" cssClass="control-label required">
        <spring:message code="label.email" />
      </form:label>
      <div class="controls">
        <form:input path="email.value" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Not a valid email</span>
        </span>
      </div>
    </div>

    <!-- Confirm Email -->
    <div class="control-group">
      <form:label path="email.confirmValue" cssClass="control-label required">
        <spring:message code="label.confirmEmail" />
      </form:label>
      <div class="controls">
        <form:input path="email.confirmValue" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Emails do not match</span>
        </span>
      </div>
    </div>

    <!-- Password -->
    <div class="control-group">
      <form:label path="password.value" cssClass="control-label required">
        <spring:message code="label.password" />
      </form:label>
      <div class="controls">
        <form:password path="password.value" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Must have both numbers &amp;
            letters</span>
        </span>
      </div>
    </div>

    <!-- Confirm Password -->
    <div class="control-group">
      <form:label path="password.confirmValue" cssClass="control-label required">
        <spring:message code="label.confirmPassword" />
      </form:label>
      <div class="controls">
        <form:password path="password.confirmValue" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span class="validation"> <span class="message"></span> <span class="accept"></span> <span class="reject">Passwords do not match</span>
        </span>
      </div>
    </div>

    <!-- Buttons -->
    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
    </div>

  </fieldset>
</form:form>


<script type="text/javascript">
	$(function() {
		$("#email\\.value").enableValidation(validateEmail).enableConfirmField();
		$("#password\\.value").enableValidation(validatePassword).enableConfirmField();
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>