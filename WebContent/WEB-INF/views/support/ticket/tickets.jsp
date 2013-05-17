<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

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
  <c:out value="${ticketList.size()}" />
  )
</h3>

<c:if test="${not empty ticketSearchContextString}">
  <div class="info">
    <p>${ticketSearchContextString}</p>
  </div>
</c:if>

<c:choose>
  <c:when test="${not empty ticketList}">
    <table>
      <tr>
        <th>Id</th>
        <th>Title</th>
        <th>Category</th>
        <th>Priority</th>
        <th>Status</th>
        <th style="text-align: right;">Created Date</th>
      </tr>
      <c:forEach var="ticket" items="${ticketList}">
        <tr>
          <td><a href="<spring:url value="/support/ticket/view/${ticket.id}" />">${ticket.id}</a></td>
          <td class="ticketTitle" style="position: relative;"><c:if test="${ticket.type == 'INQUIRY'}">
              <img style="vertical-align: middle; margin-left: -18px;" src="<spring:url value="/static/images/icons/help.png" />" alt="Inquiry Ticket" />
            </c:if> <a style="text-decoration: none;" href="<spring:url value="/support/ticket/view/${ticket.id}"  />">${ticket.title}</a>
            <div class="ticketSummary"
              style="border: 1px solid #dc6f64; margin-left: 8px; padding: 5px; background: #FCFFE6; box-shadow: 1px 1px 10px #999; -moz-box-shadow: 1px 1px 10px #999; -webkit-box-shadow: 1px 1px 10px #999; border-radius: 4px; z-index: 20; position: absolute; top: 15px; right: -350px; width: 400px; display: none;">
              <span style="font-weight: bold;">${ticket.title}</span><span style="position: absolute; top: 3px; right: 3px;">ID: ${ticket.id}</span>
              <p>${ticket.description}</p>
            </div></td>
          <td>${fn:toLowerCase(ticket.category.description)}</td>
          <c:choose>
            <c:when test="${ticket.priority == 'HIGH' || ticket.priority == 'VERY_HIGH'}">
              <td style="color: red"><c:out value="${fn:toLowerCase(ticket.priority)}" /></td>
            </c:when>
            <c:otherwise>
              <td>${fn:toLowerCase(ticket.priority)}</td>
            </c:otherwise>
          </c:choose>
          <td>${fn:toLowerCase(ticket.status)}</td>
          <td style="text-align: right;"><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
        </tr>
      </c:forEach>
    </table>
  </c:when>

  <c:otherwise>
    <p>No tickets found.</p>
  </c:otherwise>

</c:choose>

<script type="text/javascript">
	$(function() {
		$(".ticketTitle").hover(function() {
			$(this).find(".ticketSummary").show();
		}, function() {
			$(this).find(".ticketSummary").hide();
		});
	});
</script>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>