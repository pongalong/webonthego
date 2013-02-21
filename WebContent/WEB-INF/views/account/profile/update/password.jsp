
<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Change Password</h3>

  <p>Please choose a password with at least 5 characters (at least one letter and one number).</p>

  <form:form id="updatePassword" method="POST" commandName="updatePassword" cssClass="validatedForm">
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

    <c:if test="${CONTROLLING_USER.userId <= 0}">
      <div class="row clearfix">
        <form:label path="oldPassword" cssClass="required">Old Password</form:label>
        <form:password path="oldPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>
    </c:if>

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
      <a href="<spring:url value="/profile" />" class="mBtn">Cancel </a> <input type="submit" value="Update Password"></input>
    </div>

  </form:form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>


<%@ include file="/WEB-INF/views/include/footer.jsp"%>