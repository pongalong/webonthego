<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Create User</h3>

<form:form id="createRep" cssClass="validatedForm" commandName="newInternalUser" method="post">

  <!-- Errors -->
  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.newInternalUser'].allErrors}">
    <div class="row clearfix">
      <div class="alert error">
        <h1>Please correct the following problems</h1>
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
    </div>
  </c:if>

  <div class="row clearfix">
    <form:label path="username">Username</form:label>
    <form:input path="username" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <form:label path="email" cssClass="required">Email</form:label>
    <form:input path="email" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <label class="required">Role</label> <select name="user_role" class="span-8" style="width: 312px;">
      <c:forEach var="availableRole" items="${availableRoles}">
        <c:if test="${availableRole != 'ROLE_USER' && availableRole != 'ROLE_ANONYMOUS' }">
          <option value="${availableRole}">${availableRole.name}</option>
        </c:if>
      </c:forEach>
    </select>
  </div>

  <div class="row clearfix">
    <form:label path="password" cssClass="required">Password</form:label>
    <form:input path="password" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="buttons">
    <input type="submit" value="Create" />
  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>