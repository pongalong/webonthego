<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="ticketNote" method="post">
  <fieldset>
    <legend>Reply to Ticket ${ticket.id}</legend>

    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticketNote'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="note" />
        <spring:bind path="ticketNote">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <table class="info_table_form">
      <tr>
        <td>Email:</td>
        <td>${contactEmail}</td>
      </tr>
      <c:if test="${ticket.type == 'INQUIRY'}">
        <tr>
          <td>Phone:</td>
          <td>${ticket.contactPhone}</td>
        </tr>
      </c:if>
      <tr>
        <td>Title:</td>
        <td>${ticket.title}</td>
      </tr>
      <tr>
        <td>Description:</td>
        <td style="white-space: pre-wrap;">${ticket.description}</td>
      </tr>
    </table>

    <form:label path="note" cssClass="control-label">Notes</form:label>
    <form:textarea path="note" cssClass="span10" cssErrorClass="span10 validationFailed" cssStyle="min-height: 150px;" />

    <div class="right">
      <button type="button" class="button" onclick="location.href='../view/${ticket.id}'">Cancel</button>
      <button type="submit" class="button">Send Reply</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>