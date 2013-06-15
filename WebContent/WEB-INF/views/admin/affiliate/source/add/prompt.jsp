<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="sourceCode" cssClass="form-horizontal">
  <fieldset>
    <legend>Add an Affiliate Source Code</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.sourceCode'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="name" />
        <form:errors path="code" />
        <spring:bind path="sourceCode">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <p class="well">Add and assign a source code to an affiliate.</p>

    <div class="control-group">
      <form:label path="parent.id" cssClass="control-label">Parent</form:label>
      <div class="controls">
        <form:select path="parent.id" cssClass="span6" cssErrorClass="span6 validationFailed">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <form:options items="${sourceCodeList}" itemValue="id" itemLabel="name" />
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="name" cssClass="control-label required">Name</form:label>
      <div class="controls">
        <form:input path="name" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="code" cssClass="control-label required">Code</form:label>
      <div class="controls">
        <form:input path="code" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="description" cssClass="control-label">Description</form:label>
      <div class="controls">
        <form:textarea path="description" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <div class="controls">
      <button type="button" class="button" onclick="location.href='<spring:url value="/admin/affiliate" />'">Cancel</button>
      <button type="submit" class="button">Save</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>