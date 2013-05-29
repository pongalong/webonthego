<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<div class="span12">

  <form:form id="updatePassword" method="POST" commandName="updatePassword" cssClass="form-horizontal">
    <fieldset>
      <legend>Update Your Password</legend>

      <!--Begin Error Display -->
      <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updatePassword'].allErrors}">
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <h4>Please correct the following problems</h4>
          <form:errors path="oldPassword" />
          <form:errors path="newPassword" />
          <form:errors path="confirmNewPassword" />
        </div>
      </c:if>
      <!--End Error Display -->

      <p>Enter a new password for your account.</p>

      <div class="control-group">
        <form:label path="newPassword" cssClass="control-label">New Password</form:label>
        <div class="controls">
          <form:password path="newPassword" cssClass="span6" cssErrorClass="span6 validationFailed" />
        </div>
      </div>

      <div class="control-group">
        <form:label path="confirmNewPassword" cssClass="control-label">Confirm Password</form:label>
        <div class="controls">
          <form:password path="confirmNewPassword" cssClass="span6" cssErrorClass="span6 validationFailed" />
        </div>
      </div>

      <div class="controls">
        <button type="submit" class="button">Update Password</button>
      </div>

    </fieldset>
  </form:form>
</div>

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>