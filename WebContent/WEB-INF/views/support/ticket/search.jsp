<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="searchTicket" method="post" cssClass="form-horizontal">

  <fieldset>
    <legend>
      Search Tickets
      <c:choose>
        <c:when test="${searchTicket.customerId > 0}">
            for ${sessionScope.USER.username} ${sessionScope.USER.userId }
          </c:when>
        <c:otherwise>
            for All Users
        </c:otherwise>
      </c:choose>
    </legend>

    <div class="well">
      <p>Search for tickets using the below controls.</p>
      <c:if test="${searchTicket.customerId > 0}">
        <p>
          <strong>You are searching for tickets opened by ${sessionScope.USER.username} (User ID: ${sessionScope.USER.userId })</strong>
        </p>
      </c:if>
    </div>

    <div class="control-group">
      <form:label path="creatorId" cssClass="control-label">By Me</form:label>
      <div class="controls">
        <form:checkbox path="creatorId" value="${sessionScope.CONTROLLING_USER.userId}" />
        <input type="hidden" value="0" name="creatorId" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="assigneeId" cssClass="control-label">Assigned to me</form:label>
      <div class="controls">
        <form:checkbox path="assigneeId" value="${sessionScope.CONTROLLING_USER.userId}" />
        <input type="hidden" value="0" name="assigneeId" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="status" cssClass="control-label">With Status</form:label>
      <div class="controls">
        <form:select path="status" cssClass="span5" cssErrorClass="span5 validationFailed">
          <form:options items="${statusList}" itemLabel="description" />
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="priority" cssClass="control-label">With Priority</form:label>
      <div class="controls">
        <form:select path="priority" cssClass="span5" cssErrorClass="span5 validationFailed">
          <form:options items="${priorityList}" itemLabel="description" />
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="category.id" cssClass="control-label">In Category</form:label>
      <div class="controls">
        <form:select path="category.id" cssClass="span5" cssErrorClass="span5 validationFailed">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <c:forEach var="cat" items="${ticketCategories}">
            <optgroup label="${cat.description}">
              <form:options items="${cat.subcategories}" itemValue="id" itemLabel="description" />
            </optgroup>
          </c:forEach>
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="title" cssClass="control-label">Title</form:label>
      <div class="controls">
        <form:input path="title" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="description" cssClass="control-label">Description</form:label>
      <div class="controls">
        <form:input path="description" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button">Search</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>