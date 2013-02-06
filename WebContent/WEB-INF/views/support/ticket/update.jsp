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


        <form:form id="updateTicket" cssClass="validatedForm" method="post" commandName="ticket">

          <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Edit Ticket ${ticket.id} Details</h3>

          <div class="tableContainer">

            <table>

              <tr>
                <c:if test="${ticket.type != 'CUSTOMER' && ticket.type != 'INQUIRY'}">
                  <td><form:label path="creatorId">
                      <b>Creator</b>
                    </form:label></td>
                  <td><form:input path="creatorId" cssClass="span-8" cssErrorClass="span-8 validationFailed" /></td>
                </c:if>
              </tr>

              <c:if test="${ticket.type != 'INQUIRY'}">
                <tr>
                  <td><form:label path="customerId">
                      <b>Customer</b>
                    </form:label></td>

                  <td><input type="text" value="${customer.email}" class="span-8" readOnly /> <form:input path="customerId" cssClass="span-8 hidden"
                      cssErrorClass="span-8 validationFailed hidden" /></td>
                </tr>
              </c:if>

              <c:if test="${ticket.type != 'INQUIRY'}">
                <tr>
                  <td><form:label path="assigneeId">
                      <b>Assigned To</b>
                    </form:label></td>

                  <td><form:select path="assigneeId" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
                      <form:option value="0">Select one</form:option>
                      <c:forEach var="internalUser" items="${internalUsers}">
                        <form:option value="${internalUser.userId}">${internalUser.username}</form:option>
                      </c:forEach>
                    </form:select></td>
                </tr>
              </c:if>

              <tr>
                <td><form:label path="priority">
                    <b>Priority</b>
                  </form:label></td>

                <td><form:select path="priority" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
                    <c:forEach var="priority" items="${priorityList}" varStatus="status">
                      <form:option value="${priority}">${priority.description}</form:option>
                    </c:forEach>
                  </form:select></td>
              </tr>

              <tr>
                <td><form:label path="category">
                    <b>Category</b>
                  </form:label></td>

                <td><form:select path="category" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
                    <c:forEach var="category" items="${categoryList}" varStatus="status">
                      <form:option value="${category}">${category.description}</form:option>
                    </c:forEach>
                  </form:select></td>
              </tr>
            </table>


            <table>

              <tr>
                <td><form:label path="title">
                    <b>Title</b>
                  </form:label></td>

                <td><form:input path="title" cssClass="span-8" cssErrorClass="span-8 validationFailed" /></td>
              </tr>

              <tr>
                <td><form:label path="status">
                    <b>Status</b>
                  </form:label></td>

                <td><form:select path="status" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
                    <c:forEach var="status" items="${statusList}">
                      <form:option value="${status}">${status.description}</form:option>
                    </c:forEach>
                  </form:select></td>
              </tr>

              <tr>
                <td><form:label path="description">
                    <b>Description</b>
                  </form:label></td>

                <td><form:input path="description" cssClass="span-8" cssErrorClass="span-8 validationFailed" /></td>
              </tr>

            </table>

            <table>
              <tr>
                <td><form:label path="createdDate">
                    <b>Creation Date</b>
                  </form:label></td>

                <td><input type="text" value="${ticket.createdDate}" class="span-8" readOnly /></td>
              </tr>
              <tr>
                <td><form:label path="lastModifiedDate">
                    <b>Last Modified Date</b>
                  </form:label></td>

                <td><input type="text" value="${ticket.lastModifiedDate}" class="span-8" readOnly /></td>
              </tr>
            </table>

          </div>

          <div class="buttons">
            <a id="updateTicket_button_submit" href="#" class="button action-m"><span>Update</span></a> <a
              href="<spring:url value="/support/ticket/view/ticket/${ticket.id}" />" class="button action-m multi"><span>Cancel</span></a> <input
              id="updateTicket_submit" type="submit" name="_eventId_submit" value="Submit" class="hidden" />
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