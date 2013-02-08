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
    <div class="mainbody">
      <div class="span-18 colborder" style="min-height: 200px;">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Ticket ${ticket.id}</h3>

        <div class="tableContainer">

          <c:if test="${ticketNote.ticket.type == 'INQUIRY'}">
            <table style="border: 1px #ccc solid; border-radius: 4px;">
              <tr>
                <td>Email:</td>
                <td class="value">${ticketNote.ticket.contactEmail}</td>
              </tr>
              <tr>
                <td>Phone:</td>
                <td class="value">${ticketNote.ticket.contactPhone}</td>
              </tr>
            </table>
          </c:if>


          <table style="border: 1px #ccc solid; border-radius: 4px;">
            <tr>
              <td>Title:</td>
              <td class="value">${ticketNote.ticket.title}</td>
            </tr>
            <tr>
              <td>Description:</td>
              <td class="value">${ticketNote.ticket.description}</td>
            </tr>
            <tr>
              <td>Category:</td>
              <td class="value">${ticketNote.ticket.category.description}</td>
            </tr>
          </table>

        </div>

        <form:form id="createTicketNote" cssClass="validatedForm" method="post" commandName="ticketNote">
          <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Note</h3>
          <!-- Error Alert -->
          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticketNote'].allErrors}">
            <div class="row">
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

          <div class="row" style="text-align: center;">
            <form:textarea rows="5" cols="30" path="note" cssErrorClass="validationFailed" cssStyle="resize: vertical; width:97%; height: 150px; margin: auto;" />
          </div>

          <div class="buttons">
            <a href="<spring:url value="/support/ticket/view/ticket/${ticket.id}" />" class="mBtn"><span>Cancel</span></a> <input type="submit"
              name="_eventId_submit" value="Add" />
          </div>

        </form:form>

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