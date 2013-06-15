<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Ticket ${ticket.id} Detail</h3>

<!-- Begin Contact Information -->
<c:choose>
  <c:when test="${ticket.type != 'INQUIRY'}">
    <table class="info_table_form">
      <c:if test="${not empty customer}">
        <tr>
          <td>Customer:</td>
          <td>${customer.email} (${customer.userId})</td>
        </tr>
      </c:if>

      <tr>
        <td>Assigned to:</td>
        <td><c:choose>
            <c:when test="${not empty assignee}">
              ${assignee.username} (${assignee.userId})
            </c:when>
            <c:otherwise>
              Unassigned
            </c:otherwise>
          </c:choose></td>
      </tr>

      <c:if test="${not empty creator}">
        <tr>
          <td>Creator:</td>
          <td>${creator.email} (${creator.userId})</td>
        </tr>
      </c:if>
    </table>
  </c:when>
  <c:otherwise>
    <table class="info_table_form">
      <tr>
        <td>Email:</td>
        <td>${ticket.contactEmail}</td>
      </tr>
      <tr>
        <td>Phone:</td>
        <td>${ticket.contactPhone}</td>
      </tr>
    </table>
  </c:otherwise>
</c:choose>



<!-- Begin Ticket Information -->
<table class="info_table_form">
  <tr>
    <td>Title:</td>
    <td>${ticket.title}</td>
  </tr>
  <tr>
    <td>Description:</td>
    <td style="white-space: pre-wrap;">${ticket.description}</td>
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
</table>
<!--  End Ticket Information -->

<!-- Begin Date Information -->
<table class="info_table_form">
  <tr>
    <td>Created Date:</td>
    <td><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
  </tr>
  <tr>
    <td>Last Modified Date:</td>
    <td><fmt:formatDate type="date" value="${ticket.lastModifiedDate}" /></td>
  </tr>
</table>
<!-- End Date Information -->

<!-- Begin Notes -->
<h3>Notes</h3>
<c:choose>
  <c:when test="${not empty ticket.notes}">
    <table>
      <tr>
        <th>Date</th>
        <th>Author</th>
        <th>Note</th>
      </tr>
      <c:forEach var="note" items="${ticket.notes}">
        <tr>
          <td><a href="<spring:url value="/support/ticket/note/update/${note.id}" />"><fmt:formatDate type="date" value="${note.date}" /></a></td>
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
<!-- End Notes -->

<div class="buttons align-right">
  <a href="../update/${ticket.id}" class="button">Update</a> <a href="../note/add/${ticket.id}" class="button">Add a Note</a> <a href="../reply/${ticket.id}"
    class="button">Reply</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>