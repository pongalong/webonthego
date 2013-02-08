<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/mousePositionPopup.js" />"></script>
<script type="text/javascript" src="<spring:url value="/static/javascript/infoIconPopup.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder" style="min-height: 200px;">

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">
          Tickets
          <c:choose>
            <c:when test="${not empty sessionScope.USER && sessionScope.USER.userId != -1}">
            for ${sessionScope.USER.username}
          </c:when>
            <c:otherwise>
            for All Users
            </c:otherwise>
          </c:choose>
        </h3>
        <p style="margin: 0 0 20px 0; padding: 0;">${ticketSearchContextString}</p>

        <c:choose>
          <c:when test="${not empty ticketList}">
            <table>
              <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Status</th>
                <th>Category</th>
                <th>Priority</th>
                <th>Created Date</th>
              </tr>
              <c:forEach var="ticket" items="${ticketList}">
                <tr>
                  <td><a href="<spring:url value="/support/ticket/view/ticket/${ticket.id}" />">${ticket.id}</a></td>
                  <td>${ticket.title}</td>
                  <td>${fn:toLowerCase(ticket.status)}</td>
                  <td>${fn:toLowerCase(ticket.category)}</td>
                  <c:choose>
                    <c:when test="${ticket.priority == 'HIGH' || ticket.priority == 'VERY_HIGH'}">
                      <td style="color: red"><c:out value="${fn:toLowerCase(ticket.priority)}" /></td>
                    </c:when>
                    <c:otherwise>
                      <td>${fn:toLowerCase(ticket.priority)}</td>
                    </c:otherwise>
                  </c:choose>
                  <td><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
                </tr>
              </c:forEach>
            </table>
          </c:when>
          <c:otherwise>
            <p>No tickets found.</p>
          </c:otherwise>
        </c:choose>

      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/admin/navigation/adminNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>
    <!-- Close main-content -->

  </div>
  <!-- Close container -->

  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>

</body>
</html>