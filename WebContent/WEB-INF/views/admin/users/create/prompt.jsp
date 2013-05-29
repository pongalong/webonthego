<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="newInternalUser" method="post" cssClass="form-horizontal">
  <fieldset>
    <legend>Create User</legend>

    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.newInternalUser'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="username" />
        <form:errors path="email" />
        <form:errors path="password" />
        <!-- Global Errors -->
        <spring:bind path="newInternalUser">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <p>Create an internal user with the given role. Users are only able to create new accounts with roles below their own.</p>

    <div class="control-group">
      <form:label path="username" cssClass="control-label">Username</form:label>
      <div class="controls">
        <form:input path="username" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="email" cssClass="control-label required">Email</form:label>
      <div class="controls">
        <form:input path="email" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <label class="control-label required">Role</label>
      <div class="controls">
        <select name="user_role" class="span5">
          <c:forEach var="availableRole" items="${availableRoles}">
            <c:if test="${availableRole != 'ROLE_USER' && availableRole != 'ROLE_ANONYMOUS' }">
              <option value="${availableRole}">${availableRole.name}</option>
            </c:if>
          </c:forEach>
        </select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="password" cssClass="control-label required">Password</form:label>
      <div class="controls">
        <form:input path="password" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button">Create</button>
    </div>

  </fieldset>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>