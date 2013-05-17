<%@ include file="/WEB-INF/views/include/header.jsp"%>

<h3>Update Your Password</h3>

<form:form id="updatePassword" cssStyle="padding-bottom: 0;" method="POST" commandName="updatePassword" cssClass="validatedForm">

  <!--Begin Error Display -->
  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updatePassword'].allErrors}">
    <div class="alert error">
      <h1>Please correct the following problems</h1>
      <form:errors path="oldPassword" />
      <form:errors path="newPassword" />
      <form:errors path="confirmNewPassword" />
    </div>
  </c:if>
  <!--End Error Display -->

  <div class="alert info" style="margin-bottom: 0;">

    <p>Enter a new password for your account.</p>

    <div class="row clearfix hidden">
      <form:password path="oldPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:password path="newPassword" placeholder="New Password" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:password path="confirmNewPassword" placeholder="Confirm Password" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <p>
      <input type="submit" value="Update Password"></input>
    </p>

  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>