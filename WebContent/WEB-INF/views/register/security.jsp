<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18">

  <form:form id="registration_security" cssClass="validatedForm" method="post" commandName="simpleRegistrationSecurity">

    <h3>Setup ID Verification</h3>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.simpleRegistrationSecurity'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="securityQuestionAnswer.id" />
          <form:errors path="securityQuestionAnswer.answer" />
          <form:errors path="captcha.value" />
          <!-- Global Errors -->
          <spring:bind path="simpleRegistrationSecurity">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <p>If you lose your password you can confirm your identity with your security question to reset your password.</p>

    <!-- Security question -->
    <div class="row clearfix">
      <form:label path="securityQuestionAnswer.id" cssClass="required">Security Question </form:label>
      <form:select cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;" path="securityQuestionAnswer.id">
        <form:option value="0">
          <spring:message code="label.selectOne" />
        </form:option>
        <c:forEach var="question" items="${questions}">
          <form:option value="${question.id}">
            <c:out value="${question.question}" />
          </form:option>
        </c:forEach>
      </form:select>
      <span class="validation"><span class="message"></span><span class="accept"></span><span class="reject"></span></span>
    </div>
    <div class="row clearfix">
      <form:label path="securityQuestionAnswer.answer" cssClass="required">
        <spring:message code="label.hintAnswer" />
      </form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="securityQuestionAnswer.answer" />
      <span class="validation"><span class="message"></span><span class="accept"></span><span class="reject"></span></span>
    </div>

    <!-- Jcaptcha -->
    <div class="row clearfix" style="margin-bottom: 0px; padding-bottom: 0px;">
      <form:label path="captcha.value" cssClass="required">Security Verification </form:label>
      <div style="border: 1px #bbb solid; width: 310px; text-align: center; float: left; background: white;">
        <span style="color: #666; float: left; margin-left: 5px;">Enter the text in the image below</span> <img id="jCaptchaImage"
          src="<spring:url value='/static/images/jcaptcha.jpg' htmlEscape='true' />" alt="Security image" />
      </div>
    </div>
    <div class="row clearfix pushed" style="margin-top: -5px; padding-top: 0px;">
      <div style="width: 300px; text-align: right;">
        <a href="#" class="captchaReload" tabindex="-1">request another image</a>
      </div>
    </div>
    <div class="row clearfix">
      <form:label path="captcha.value" cssClass="required">Image Text </form:label>
      <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" autocomplete="off" path="captcha.value" />
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Continue" />
    </div>

  </form:form>

</div>


<script type="text/javascript" src="<spring:url value='/static/javascript/jCaptcha.js' />"></script>

<script type="text/javascript">
	$(function() {
		$("#securityQuestionAnswer\\.id").enableValidation(validateSelected);
		$("#securityQuestionAnswer\\.answer").enableValidation(validateNotEmpty);
	});
</script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>