<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="registrationSecurity" method="post" cssClass="form-horizontal">
  <fieldset>
    <legend>Setup ID Verification</legend>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.registrationSecurity'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="securityQuestionAnswer.id" />
        <form:errors path="securityQuestionAnswer.answer" />
        <form:errors path="captcha.value" />
        <!-- Global Errors -->
        <spring:bind path="registrationSecurity">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="well">
      <p>If you lose your password you can confirm your identity with your security question to reset your password.</p>
    </div>

    <!-- Security question -->
    <div class="control-group">
      <form:label path="securityQuestionAnswer.id" cssClass="control-label required">Security Question </form:label>
      <div class="controls">
        <form:select path="securityQuestionAnswer.id" cssClass="span6" cssErrorClass="span6 validationFailed">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <form:options items="${questions}" itemValue="id" itemLabel="question" />
        </form:select>
        <span class="validation"><span class="message"></span><span class="accept"></span><span class="reject"></span></span>
      </div>
    </div>

    <div class="control-group">
      <form:label path="securityQuestionAnswer.answer" cssClass="control-label required">
        <spring:message code="label.hintAnswer" />
      </form:label>
      <div class="controls">
        <form:input path="securityQuestionAnswer.answer" cssClass="span6" cssErrorClass="span6 validationFailed" />
        <span class="validation"><span class="message"></span><span class="accept"></span><span class="reject"></span></span>
      </div>
    </div>

    <!-- Jcaptcha -->
    <div class="control-group">
      <form:label path="captcha.value" cssClass="control-label required">Security Verification</form:label>
      <div class="controls">
        <form:input path="captcha.value" autocomplete="off" placeholder="Enter the below text" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
      <div class="controls">
        <div class="span6 captcha">
          <img id="jCaptchaImage" src="<spring:url value='/static/images/jcaptcha.jpg' htmlEscape='true' />" alt="Security image" />
        </div>
      </div>
      <div class="controls">
        <a href="#" class="span6 captchaReload" style="margin-left: 0;" tabindex="-1">request another image</a>
      </div>
    </div>

    <!-- Buttons -->
    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
    </div>

  </fieldset>
</form:form>

<script type="text/javascript" src="<spring:url value='/static/javascript/jCaptcha.js' />"></script>

<script type="text/javascript">
	$(function() {
		$("#securityQuestionAnswer\\.id").enableValidation(validateSelected);
		$("#securityQuestionAnswer\\.answer").enableValidation(validateNotEmpty);
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>