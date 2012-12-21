<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>TruConnect Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder" style="min-height: 200px;">


        <form:form id="updateTicket" cssClass="validatedForm" method="post" commandName="ticket">

          <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Edit Ticket Details</h3>

          <div class="row">
            <form:label path="title">Ticket Id</form:label>
            ${ticket.id}
          </div>
          
          <c:if test="${ticket.type != 'CUSTOMER'}">
            <div class="row">
              <form:label path="creatorId">Creator</form:label>
              ${ticket.creatorId}
            </div>
          </c:if>

          <div class="row">
            <form:label path="customerId">Customer</form:label>
            ${ticket.customerId}
          </div>

          <div class="row">
            <form:label path="createdDate">Creation Date</form:label>
            ${ticket.createdDate}
          </div>

          <div class="row">
            <form:label path="lastModifiedDate">Last Modified Date</form:label>
            ${ticket.lastModifiedDate}
          </div>

          <div class="row">
            <form:label path="title">Title</form:label>
            <form:input path="title" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <div class="row">
            <form:label path="status">Status</form:label>
            <form:select path="status" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
              <c:forEach var="status" items="${statusList}">
                <form:option value="${status}">${status.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="category">Category</form:label>
            <form:select path="category" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
              <c:forEach var="category" items="${categoryList}" varStatus="status">
                <form:option value="${category}">${category.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="priority">Priority</form:label>
            <form:select path="priority" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
              <c:forEach var="priority" items="${priorityList}" varStatus="status">
                <form:option value="${priority}">${priority.description}</form:option>
              </c:forEach>
            </form:select>
          </div>

          <div class="row">
            <form:label path="description">Description</form:label>
            <form:input path="description" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <div class="row">
            <form:label path="assigneeId">Assigned To</form:label>
            <form:input path="assigneeId" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>


          <div class="buttons">
            <a id="updateTicket_button_submit" href="#" class="button action-m"><span>Update</span></a> <input id="updateTicket_submit" type="submit"
              name="_eventId_submit" value="Submit" class="hidden" />
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