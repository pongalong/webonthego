<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>



<form:form method="POST" commandName="updatePassword" cssClass="form-horizontal">

  <fieldset>
    <legend>Change Password</legend>

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

    <p>Please choose a password with at least 5 characters (at least one letter and one number).</p>

    <c:if test="${CONTROLLING_USER.userId <= 0}">
      <div class="control-group">
        <form:label path="oldPassword" cssClass="control-label required">Old Password</form:label>
        <div class="controls">
          <form:password path="oldPassword" cssClass="span4" cssErrorClass="span4 validationFailed" />
        </div>
      </div>
    </c:if>

    <div class="control-group">
      <form:label path="newPassword" cssClass="control-label required">New Password</form:label>
      <div class="controls">
        <form:password path="newPassword" cssClass="span4" cssErrorClass="span4 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="confirmNewPassword" cssClass="control-label required">Confirm Password</form:label>
      <div class="controls">
        <form:password path="confirmNewPassword" cssClass="span4" cssErrorClass="span4 validationFailed" />
      </div>
    </div>


    <div class="controls">
      <button type="button" name="cancel" class="button" onclick="location.href='<spring:url value="/profile" />'">Cancel</button>
      <button type="submit" name="_eventId_submit" class="button">Update Password</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>