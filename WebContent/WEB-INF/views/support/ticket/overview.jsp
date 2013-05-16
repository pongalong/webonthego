<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <div style="margin-bottom: 40px;">
    <h3>Quick Links</h3>
    <p>
      <a href="<spring:url value="/support/ticket/view/creator/me" />" class="mBtn">Tickets I Created</a> <a
        href="<spring:url value="/support/ticket/view/assignee/me" />" class="mBtn">Tickets Assigned to Me</a> <a
        href="<spring:url value="/support/ticket/view/inquiry" />" class="mBtn">Inquiries</a>
    </p>
  </div>
  <div class="clear"></div>

  <div style="margin-bottom: 40px;">
    <h3>
      Create Tickets
      <c:choose>
        <c:when test="${sessionScope.USER.userId > 0}">
            for ${sessionScope.USER.username} ${sessionScope.USER.userId }
          </c:when>
        <c:otherwise>
            for All Users
            </c:otherwise>
      </c:choose>
    </h3>
    <p>
      <c:choose>
        <c:when test="${sessionScope.USER.userId > 0}">
          <a href="<spring:url value="/support/ticket/create" />" class="mBtn"><span>Create</span></a>
        </c:when>
        <c:otherwise>
          <a href="<spring:url value="/support/inquire/create" />" class="mBtn"><span>Inquire</span></a>
        </c:otherwise>
      </c:choose>
    </p>
  </div>
  <div class="clear"></div>

  <div>
    <h3>
      Search Tickets
      <c:choose>
        <c:when test="${sessionScope.USER.userId > 0}">
            for ${sessionScope.USER.username} ${sessionScope.USER.userId }
          </c:when>
        <c:otherwise>
            for All Users
            </c:otherwise>
      </c:choose>
    </h3>

    <form:form id="searchTicket" cssClass="validatedForm" method="post" commandName="ticket">

      <div class="row clearfix" style="margin-top: 10px;">
        <form:label path="creatorId">By Me</form:label>
        <input id="creatorIdCheckbox" type="checkbox" value="${sessionScope.CONTROLLING_USER.userId}"></input>
        <form:input path="creatorId" value="0" type="hidden" />
      </div>

      <div class="row clearfix">
        <form:label path="assigneeId">Assigned to me</form:label>
        <input id="assigneeIdCheckbox" type="checkbox" value="${sessionScope.CONTROLLING_USER.userId}"></input>
        <form:input path="assigneeId" value="0" type="hidden" />
      </div>

      <div class="row clearfix">
        <form:label path="status">With Status</form:label>
        <form:select path="status" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width: 312px;">
          <c:forEach var="status" items="${statusList}">
            <form:option value="${status}">${status.description}</form:option>
          </c:forEach>
        </form:select>
      </div>

      <div class="row clearfix">
        <form:label path="priority">With Priority</form:label>
        <form:select path="priority" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width: 312px;">
          <c:forEach var="priority" items="${priorityList}">
            <form:option value="${priority}">${priority.description}</form:option>
          </c:forEach>
        </form:select>
      </div>

      <div class="row clearfix">
        <form:label path="category.id" cssClass="required">Ticket Category</form:label>
        <form:select path="category.id" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
          <option value="0">Select one...</option>
          <c:forEach var="cat" items="${ticketCategories}">
            <c:if test="${cat.id > 0}">
              <optgroup label="${cat.description}">
                <c:forEach var="subcategory" items="${cat.subcategories}">
                  <form:option value="${subcategory.id}">${subcategory.description}</form:option>
                </c:forEach>
              </optgroup>
            </c:if>
          </c:forEach>
        </form:select>
      </div>

      <div class="row clearfix">
        <form:label path="title">Title</form:label>
        <form:input path="title" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>

      <div class="row clearfix">
        <form:label path="description">Description</form:label>
        <form:input path="description" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
      </div>

      <div class="buttons">
        <input type="submit" name="_eventId_submit" />
      </div>

    </form:form>
  </div>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/pages/ticketSearch.js' />"></script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>