<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form method="POST" commandName="updateEmail" cssClass="form-horizontal">
  <fieldset>

    <legend>Change E-Mail Address</legend>

    <!--Begin Error Display -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updateEmail'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="oldPassword" />
        <form:errors path="newEmail" />
        <form:errors path="confirmNewEmail" />
      </div>
    </c:if>
    <!--End Error Display -->

    <c:if test="${CONTROLLING_USER.userId <= 0}">
      <div class="control-group">
        <form:label path="oldPassword" cssClass="control-label required">Password</form:label>
        <div class="controls">
          <form:password path="oldPassword" cssClass="span4" cssErrorClass="span4 validationFailed" />
        </div>
      </div>
    </c:if>

    <div class="control-group">
      <form:label path="newEmail" cssClass="control-label required">New E-Mail Address</form:label>
      <div class="controls">
        <form:input path="newEmail" cssClass="span4" cssErrorClass="span4 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="confirmNewEmail" cssClass="control-label required">Confirm E-Mail Address</form:label>
      <div class="controls">
        <form:input path="confirmNewEmail" cssClass="span4" cssErrorClass="span4 validationFailed" />
      </div>
    </div>


    <div class="controls">
      <button type="button" name="cancel" class="button" onclick="location.href='<spring:url value="/profile" />'">Cancel</button>
      <button type="submit" name="_eventId_submit" class="button">Update Email</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>