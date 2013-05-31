<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<c:choose>

  <c:when test="${USER.userId > 0}">

    <form:form commandName="ticket" method="post" cssClass="form-horizontal">
      <fieldset>
        <legend>Open a Ticket for ${USER.email}</legend>

        <!-- Error Alert -->
        <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticket'].allErrors}">
          <div class="alert alert-error">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4>Please correct the following problems</h4>
            <form:errors path="title" />
            <form:errors path="category.id" />
            <form:errors path="priority" />
            <form:errors path="description" />
            <!-- Global Errors -->
            <spring:bind path="ticket">
              <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
              </c:forEach>
            </spring:bind>
          </div>
        </c:if>

        <div class="control-group">
          <form:label path="category.id" cssClass="control-label required">Ticket Category</form:label>
          <div class="controls">
            <form:select path="category.id" cssClass="span6" cssErrorClass="span6 validationFailed">
              <form:option value="0">
                <spring:message code="label.selectOne" />
              </form:option>
              <c:forEach var="cat" items="${ticketCategories}">
                <optgroup label="${cat.description}">
                  <c:forEach var="subcategory" items="${cat.subcategories}">
                    <form:option value="${subcategory.id}">${subcategory.description}</form:option>
                  </c:forEach>
                </optgroup>
              </c:forEach>
            </form:select>
          </div>
        </div>

        <div class="control-group">
          <form:label path="title" cssClass="control-label">Short Description</form:label>
          <div class="controls">
            <form:input path="title" cssClass="span6" cssErrorClass="span6 validationFailed" />
          </div>
        </div>

        <c:if test="${CONTROLLING_USER.userId > 0}">
          <div class="control-group">
            <form:label path="priority" cssClass="control-label required">Priority</form:label>
            <div class="controls">
              <form:select path="priority" cssClass="span6" cssErrorClass="span6 validationFailed">
                <c:forEach var="priority" items="${priorityList}" varStatus="status">
                  <form:option value="${priority}">${priority.description}</form:option>
                </c:forEach>
              </form:select>
            </div>
          </div>
        </c:if>

        <div class="control-group">
          <form:label path="description" cssClass="control-label required">Long Description</form:label>
          <div class="controls">
            <form:textarea path="description" cssClass="span6" cssErrorClass="span6 validationFailed" />
          </div>
        </div>

        <div class="controls">
          <button type="button" class="button" onclick="location.href='/account'">Cancel</button>
          <button type="submit" class="button">Submit Ticket</button>
        </div>

      </fieldset>
    </form:form>

  </c:when>

  <c:otherwise>
    <p>You must create tickets with a customer account. Please navigate to the customers account before creating a ticket.</p>
  </c:otherwise>

</c:choose>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>