<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Ticket ${ticket.id} Detail</h3>

<div class="tableContainer">

  <table style="border: 1px #ccc solid; border-radius: 4px;">
    <tr>
      <td>Email:</td>
      <td class="value">${contactEmail}</td>
    </tr>
    <c:if test="${ticket.type == 'INQUIRY'}">
      <tr>
        <td>Phone:</td>
        <td class="value">${ticket.contactPhone}</td>
      </tr>
    </c:if>
  </table>

  <table style="border: 1px #ccc solid; border-radius: 4px;">
    <tr>
      <td>Description:</td>
      <td class="value">${ticket.description}</td>
    </tr>
  </table>

</div>

<form:form id="replyAndCreateTicketNote" cssClass="validatedForm" method="post" commandName="ticketNote">
  <h3>Reply</h3>
  <!-- Error Alert -->
  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticketNote'].allErrors}">
    <div class="row clearfix">
      <div class="alert error">
        <h1>Please correct the following problems</h1>
        <form:errors path="note" />
        <!-- Global Errors -->
        <spring:bind path="ticketNote">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </div>
  </c:if>

  <div class="row clearfix" style="text-align: center;">
    <form:textarea rows="5" cols="30" path="note" cssErrorClass="validationFailed" cssStyle="resize: vertical; width:97%; height: 150px; margin: auto;" />
  </div>

  <div class="buttons">
    <a href="<spring:url value="/support/ticket/view/ticket/${ticket.id}" />" class="mBtn">Cancel</a> <input type="submit" name="_eventId_submit"
      value="Continue" />
  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>