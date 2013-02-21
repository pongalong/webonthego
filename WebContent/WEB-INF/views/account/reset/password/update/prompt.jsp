<%@ include file="/WEB-INF/views/include/header.jsp"%>

<form:form id="updatePassword" method="POST" commandName="updatePassword" cssClass="validatedForm span-13">

  <h3>Enter A New Password</h3>

  <!--Begin Error Display -->
  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updatePassword'].allErrors}">
    <div class="row clearfix">
      <div class="alert error">
        <h1>Please correct the following problems</h1>
        <form:errors path="oldPassword" />
        <form:errors path="newPassword" />
        <form:errors path="confirmNewPassword" />
      </div>
    </div>
  </c:if>
  <!--End Error Display -->

  <div class="row clearfix hidden">
    <form:label path="oldPassword" cssClass="required">Old Password</form:label>
    <form:password path="oldPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <form:label path="newPassword" cssClass="required">New Password</form:label>
    <form:password path="newPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <form:label path="confirmNewPassword" cssClass="required">Confirm Password</form:label>
    <form:password path="confirmNewPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <!-- Buttons -->
  <div class="buttons">
    <input type="submit" value="Update Password"></input>
  </div>

</form:form>


<%@ include file="/WEB-INF/views/include/footer.jsp"%>
