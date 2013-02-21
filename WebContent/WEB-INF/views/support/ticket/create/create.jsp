<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <c:choose>
    <c:when test="${user.userId != 0}">
      <form:form id="createTicket" cssClass="validatedForm" method="post" commandName="ticket">
        <h3>Open a Ticket</h3>
        <!-- Error Alert -->
        <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticket'].allErrors}">
          <div class="row clearfix">
            <div class="alert error">
              <h1>Please correct the following problems</h1>
              <form:errors path="title" />
              <form:errors path="category" />
              <form:errors path="priority" />
              <form:errors path="description" />
              <!-- Global Errors -->
              <spring:bind path="ticket">
                <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                  <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                </c:forEach>
              </spring:bind>
            </div>
          </div>
        </c:if>

        <div class="row clearfix hidden">
          <c:if test="${ticket.type != 'CUSTOMER' && ticket.type != 'INQUIRY'}">
            <form:input path="creatorId" />
          </c:if>
          <form:input path="type" />
          <form:input path="assigneeId" />
          <form:input path="customerId" />
        </div>

        <div class="row clearfix">
          <form:label path="title">Ticket Title</form:label>
          <form:input path="title" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
        </div>

        <div class="row clearfix">
          <form:label path="category" cssClass="required">Category</form:label>
          <form:select path="category" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
            <c:forEach var="category" items="${categoryList}" varStatus="status">
              <form:option value="${category}">${category.description}</form:option>
            </c:forEach>
          </form:select>
        </div>

        <c:if test="${CONTROLLING_USER.userId > 0}">
          <div class="row clearfix">
            <form:label path="priority" cssClass="required">Priority</form:label>
            <form:select path="priority" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
              <c:forEach var="priority" items="${priorityList}" varStatus="status">
                <form:option value="${priority}">${priority.description}</form:option>
              </c:forEach>
            </form:select>
          </div>
        </c:if>

        <div class="row clearfix" style="height: 250px;">
          <form:label path="description" cssClass="required">Description</form:label>
          <form:textarea path="description" cssClass="span-9" cssErrorClass="span-8 validationFailed" />
        </div>

        <div class="buttons">
          <input type="submit" name="_eventId_submit" value="Create" />
        </div>

      </form:form>
    </c:when>
    <c:otherwise>
      <p>You must create tickets with a customer account. Please navigate to the customers account before creating a ticket.</p>
    </c:otherwise>
  </c:choose>


</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>