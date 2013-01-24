<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder" style="min-height: 200px;">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Ticket ${ticket.id} Detail</h3>

        <table>
          <tr>
            <td>Title:</td>
            <td>${ticket.title}</td>
          </tr>
          <tr>
            <td>Status:</td>
            <td>${ticket.status.description}</td>
          </tr>
          <tr>
            <td>Priority:</td>
            <td>${ticket.priority.description}</td>
          </tr>
          <tr>
            <td>Category:</td>
            <td>${ticket.category.description}</td>
          </tr>

          <c:if test="${ticket.type != 'INQUIRY'}">
            <tr>
              <td>Customer:</td>
              <td>${ticket.customerId}</td>
            </tr>
          </c:if>

          <c:if test="${ticket.type != 'INQUIRY'}">
            <tr>
              <td>Assigned to:</td>
              <td><c:choose>
                  <c:when test="${ticket.assigneeId > 0}">
                  ${ticket.assigneeId}
                </c:when>
                  <c:otherwise>
                  Unassigned
                </c:otherwise>
                </c:choose></td>
            </tr>
          </c:if>

          <c:if test="${ticket.type != 'CUSTOMER' && ticket.type != 'INQUIRY'}">
            <tr>
              <td>Requester:</td>
              <td>${ticket.creatorId}</td>
            </tr>
          </c:if>

          <tr>
            <td>Created Date:</td>
            <td><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
          </tr>
          <tr>
            <td>Last Modified Date:</td>
            <td><fmt:formatDate type="date" value="${ticket.lastModifiedDate}" /></td>
          </tr>
          <tr>
            <td>Description:</td>
            <td>${ticket.description}</td>
          </tr>
        </table>

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Notes</h3>
        <c:choose>
          <c:when test="${not empty ticket.notes}">
            <table>
              <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Author</th>
                <th>Note</th>
              </tr>
              <c:forEach var="note" items="${ticket.notes}">
                <tr>
                  <td><a href="<spring:url value="/support/ticket/note/update/${note.id}" />">${note.id}</a></td>
                  <td>${note.date}</td>
                  <td>${note.creator.username}</td>
                  <td>${note.note}</td>
                </tr>
              </c:forEach>
            </table>
          </c:when>
          <c:otherwise>
            <p>No notes</p>
          </c:otherwise>
        </c:choose>

        <div class="buttons">
          <a href="<spring:url value="/support/ticket" />" class="button action-m" style="float: right;"><span>Back to Tickets</span></a><a
            href="<spring:url value="/support/ticket/update/${ticket.id}" />" class="button action-m multi" style="float: right;"><span>Update Ticket</span></a>
          <a href="<spring:url value="/support/ticket/note/add/${ticket.id}" />" class="button action-m multi" style="float: right;"><span>Add a Note</span></a>
        </div>

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