<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/pages/ticketSearch.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder" style="min-height: 200px;">

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Quick Links</h3>
        <p>
          <a href="<spring:url value="/support/ticket/view/creator/me" />" class="button semi-s multi"><span>Tickets I Created</span></a> <a
            href="<spring:url value="/support/ticket/view/assignee/me" />" class="button semi-s multi"><span>Tickets Assigned to Me</span></a> <a
            href="<spring:url value="/support/ticket/view/inquiry" />" class="button semi-s"><span>Inquiries</span></a>
        </p>
        <div class="clear"></div>

        <h3 style="margin: 20px 0 10px 0; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">
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
              <a href="<spring:url value="/support/ticket/create" />" class="button semi-s multi"><span>Create</span></a>
            </c:when>
            <c:otherwise>
              <a href="<spring:url value="/support/inquire/create" />" class="button semi-s"><span>Inquire</span></a>
            </c:otherwise>
          </c:choose>
        </p>
        <div class="clear"></div>




        <h3 style="margin: 20px 0 10px 0; padding-bottom: 0px;" onClick="$(this).next('form').slideToggle();">
          <img src="<spring:url value="/static/images/buttons/icons/add.png" />" style="vertical-align: middle;" /> Search Tickets
          <c:choose>
            <c:when test="${sessionScope.USER.userId > 0}">
            for ${sessionScope.USER.username} ${sessionScope.USER.userId }
          </c:when>
            <c:otherwise>
            for All Users
            </c:otherwise>
          </c:choose>
        </h3>

        <form:form id="searchTicket" cssClass="validatedForm" method="post" commandName="ticket" style="border-top: 1px #ccc dotted;">

          <div class="row" style="margin-top: 10px;">
            <form:label path="creatorId">By Me</form:label>
            <input id="creatorIdCheckbox" type="checkbox" value="${sessionScope.CONTROLLING_USER.userId}"></input>
            <form:input path="creatorId" value="0" type="hidden" />
          </div>

          <div class="row">
            <form:label path="assigneeId">Assigned to me</form:label>
            <input id="assigneeIdCheckbox" type="checkbox" value="${sessionScope.CONTROLLING_USER.userId}"></input>
            <form:input path="assigneeId" value="0" type="hidden" />
          </div>

          <div class="row">
            <form:label path="status">With Status</form:label>
            <form:select path="status" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width: 312px;">
              <c:forEach var="status" items="${statusList}">
                <form:option value="${status}">${status.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="priority">With Priority</form:label>
            <form:select path="priority" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width: 312px;">
              <c:forEach var="priority" items="${priorityList}">
                <form:option value="${priority}">${priority.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="category">With Category</form:label>
            <form:select path="category" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width: 312px;">
              <c:forEach var="category" items="${categoryList}">
                <form:option value="${category}">${category.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="title">Title</form:label>
            <form:input path="title" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <div class="row">
            <form:label path="description">Description</form:label>
            <form:input path="description" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <div class="buttons">
            <a id="searchTicket_button_submit" href="#" class="button action-m"><span>Search</span></a> <input id="searchTicket_submit" type="submit"
              name="_eventId_submit" class="hidden" />
          </div>

        </form:form>
      </div>

      <div class="span-6 last sub-navigation">
        <%@ include file="/WEB-INF/views/include/admin/navigation/adminNav.jsp"%>
      </div>

    </div>
    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_links.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>