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
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Ticket ${ticket.id}</h3>
        <p>
          <b>Title:</b> ${ticket.title}<br /> <b>Description:</b>${ticket.description}
        </p>

        <form:form id="createTicketNote" cssClass="validatedForm" method="post" commandName="note">
          <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Note</h3>
          <!-- Error Alert -->
          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.note'].allErrors}">
            <div class="row">
              <div class="alert error">
                <h1>Please correct the following problems</h1>
                <form:errors path="note" />
                <!-- Global Errors -->
                <spring:bind path="note">
                  <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                    <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                  </c:forEach>
                </spring:bind>
              </div>
            </div>
          </c:if>


          <div class="row">
            <form:textarea rows="5" cols="30" path="note" cssErrorClass="validationFailed" cssStyle="resize: none;" />
          </div>


          <div class="buttons">
            <a id="createTicketNote_button_submit" href="#" class="button action-m"><span>Submit</span></a> <input id="createTicketNote_submit" type="submit"
              name="_eventId_submit" value="Continue" class="hidden" />
          </div>

        </form:form>

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