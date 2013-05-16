
<%@ include file="/WEB-INF/views/include/header.jsp"%>

<script type="text/javascript">
	$(function() {
		$(".ticketTitle").hover(function() {
			$(this).find(".ticketSummary").show();
		}, function() {
			$(this).find(".ticketSummary").hide();
		});
	});
</script>

<div style="min-height: 200px;">

  <h3>
    Tickets
    <c:choose>
      <c:when test="${not empty sessionScope.USER && sessionScope.USER.userId > 0}">
        for ${sessionScope.USER.username}
      </c:when>
      <c:otherwise>
        for All Users
      </c:otherwise>
    </c:choose>
    <a style="float: right;" href="<spring:url value="/support/ticket/" />">Back to Tickets</a>
  </h3>

  <p style="margin: 0 0 20px 0; padding: 0;">${ticketSearchContextString}</p>

  <c:choose>

    <c:when test="${not empty ticketList}">
      <table>
        <tr>
          <th>Id</th>
          <th>Title</th>
          <th>Category</th>
          <th>Priority</th>
          <th>Status</th>
          <th>Created Date</th>
        </tr>
        <c:forEach var="ticket" items="${ticketList}">
          <tr>
            <td><a href="<spring:url value="/support/ticket/view/ticket/${ticket.id}" />">${ticket.id}</a></td>
            <td class="ticketTitle" style="position: relative;"><c:if test="${ticket.type == 'INQUIRY'}">
                <img style="vertical-align: middle; margin-left: -18px;" src="<spring:url value="/static/images/icons/help.png" />" alt="Inquiry Ticket" />
              </c:if> <a style="text-decoration: none;" href="<spring:url value="/support/ticket/view/ticket/${ticket.id}"  />">${ticket.title}</a>
              <div class="ticketSummary"
                style="border: 1px solid #dc6f64; margin-left: 8px; padding: 5px; background: #FCFFE6; box-shadow: 1px 1px 10px #999; -moz-box-shadow: 1px 1px 10px #999; -webkit-box-shadow: 1px 1px 10px #999; border-radius: 4px; z-index: 20; position: absolute; top: 10px; right: -300px; width: 400px; display: none;">
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

<%@ include file="/WEB-INF/views/include/footer.jsp"%>