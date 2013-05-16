<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div style="min-height: 200px;">
  <h3>
    Ticket ${ticket.id} Detail <a style="float: right;" href="<spring:url value="/support/ticket/" />">Back to Tickets</a>
  </h3>

  <div class="tableContainer">

    <c:if test="${ticket.type != 'INQUIRY'}">
      <table>
        <c:if test="${not empty customer}">
          <tr>
            <td>Customer:</td>
            <td class="value">${customer.email} (${customer.userId})</td>
          </tr>
        </c:if>

        <tr>
          <td>Assigned to:</td>
          <td class="value"><c:choose>
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
            <td class="value">${creator.email} (${creator.userId})</td>
          </tr>
        </c:if>
      </table>
    </c:if>

    <c:if test="${ticket.type == 'INQUIRY'}">
      <table style="border: 1px #ccc solid; border-radius: 4px;">
        <tr>
          <td>Email:</td>
          <td class="value">${ticket.contactEmail}</td>
        </tr>
        <tr>
          <td>Phone:</td>
          <td class="value">${ticket.contactPhone}</td>
        </tr>
      </table>
    </c:if>

    <table style="border: 1px #ccc solid; border-radius: 4px;">
      <tr>
        <td>Title:</td>
        <td class="value">${ticket.title}</td>
      </tr>
      <tr>
        <td>Description:</td>
        <td class="value">${ticket.description}</td>
      </tr>
      <tr>
        <td>Status:</td>
        <td class="value">${ticket.status.description}</td>
      </tr>
      <tr>
        <td>Priority:</td>
        <td class="value">${ticket.priority.description}</td>
      </tr>
      <tr>
        <td>Category:</td>
        <td class="value">${ticket.category.description}</td>
      </tr>
    </table>

    <table style="border: 1px #ccc solid; border-radius: 4px;">
      <tr>
        <td>Created Date:</td>
        <td class="value"><fmt:formatDate type="date" value="${ticket.createdDate}" /></td>
      </tr>
      <tr>
        <td>Last Modified Date:</td>
        <td class="value"><fmt:formatDate type="date" value="${ticket.lastModifiedDate}" /></td>
      </tr>
    </table>

  </div>


  <h3>Notes</h3>
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
            <td><fmt:formatDate type="date" value="${note.date}" /></td>
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

  <div class="buttons" style="text-align: right;">
    <a href="<spring:url value="/support/ticket" />" class="mBtn">Back</a> <a href="<spring:url value="/support/ticket/update/${ticket.id}" />" class="mBtn">Update</a>
    <a href="<spring:url value="/support/ticket/note/add/${ticket.id}" />" class="mBtn">Add a Note</a> <a
      href="<spring:url value="/support/ticket/reply/${ticket.id}" />" class="mBtn">Reply</a>
  </div>

</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>