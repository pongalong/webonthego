<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Change E-Mail Address</h3>

  <form:form id="updateEmail" method="POST" action="${formAction}" commandName="updateEmail" cssClass="validatedForm">
    <!--Begin Error Display -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updateEmail'].allErrors}">
      <div class="row">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="oldPassword" />
          <form:errors path="newEmail" />
          <form:errors path="confirmNewEmail" />
        </div>
      </div>
    </c:if>
    <!--End Error Display -->

    <c:if test="${CONTROLLING_USER.userId <= 0}">
      <div class="row">
        <form:label path="oldPassword" cssClass="required">Password</form:label>
        <form:password path="oldPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>
    </c:if>

    <div class="row">
      <form:label path="newEmail" cssClass="required">New E-Mail Address</form:label>
      <form:input path="newEmail" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row">
      <form:label path="confirmNewEmail" cssClass="required">Confirm E-Mail Address</form:label>
      <form:input path="confirmNewEmail" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <a href="<spring:url value="/profile" />" class="mBtn">Cancel </a> <input type="submit" value="Update E-Mail"></input>
    </div>

  </form:form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>


<%@ include file="/WEB-INF/views/include/footer.jsp"%>
