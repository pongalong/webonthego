<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>
  <c:choose>
    <c:when test="${searchTicket.customerId > 0}">
        Tickets for ${sessionScope.USER.username} ${sessionScope.USER.userId }
      </c:when>
    <c:otherwise>
        Tickets for All Users
      </c:otherwise>
  </c:choose>
  (
  <c:out value="${ticketList.records.size()}" />
  )
</h3>

<c:if test="${not empty ticketSearchContextString}">
  <div class="alert alert-info">
    <p>${ticketSearchContextString}</p>
  </div>
</c:if>

<c:choose>
  <c:when test="${not empty ticketList}">

    <table>
      <tr>
        <th>ID</th>
        <th></th>
        <th>Title</th>
        <th>Category</th>
        <th>Priority</th>
        <th>Status</th>
        <th style="text-align: right;">Date</th>
      </tr>

      <c:forEach var="ticket" items="${ticketList.currentPage}">
        <tr>
          <td><a href="<spring:url value="/support/ticket/view/${ticket.id}" />">${ticket.id}</a></td>
          <td style="width: 16px;"><c:if test="${ticket.type == 'INQUIRY'}">
              <img style="vertical-align: middle;" src="<spring:url value="/static/images/icons/help.png" />" alt="Inquiry Ticket" />
            </c:if></td>
          <td style="max-width: 150px;"><a class="ticketTitle" href="<spring:url value="/support/ticket/view/${ticket.id}" />" data-toggle="popover"
            data-placement="right" data-original-title="${ticket.title}" data-content="${ticket.description}">${ticket.title}</a></td>
          <td>${fn:toLowerCase(ticket.category.description)}</td>
          <td><c:choose>
              <c:when test="${ticket.priority == 'HIGH' || ticket.priority == 'VERY_HIGH'}">
                <span style="color: red;">${fn:toLowerCase(ticket.priority.description)}</span>
              </c:when>
              <c:otherwise>
              ${fn:toLowerCase(ticket.priority.description)}
            </c:otherwise>
            </c:choose></td>
          <td>${fn:toLowerCase(ticket.status.description)}</td>
          <td style="text-align: right;"><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
        </tr>

      </c:forEach>

    </table>

    <c:set var="prevPageNum" value="${ticketList.currentPageNum - 1}" />
    <c:set var="nextPageNum" value="${ticketList.currentPageNum + 1}" />
    <c:if test="${prevPageNum > 0}">
      <a class="prev" href="<spring:url value="/support/ticket/search/results/${prevPageNum}" />">Previous Page</a>
    </c:if>
    <c:if test="${ticketList.currentPageNum < ticketList.pageCount}">
      <a class="next" href="<spring:url value="/support/ticket/search/results/${nextPageNum}" />">Next Page</a>
    </c:if>

  </c:when>

  <c:otherwise>
    <p>No tickets found.</p>
  </c:otherwise>

</c:choose>


<script type="text/javascript">
	$(function() {
		$(".ticketTitle").popover({
			placement : 'right',
			trigger : 'hover',
			html : true
		});
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>