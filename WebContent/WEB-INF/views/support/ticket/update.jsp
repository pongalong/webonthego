<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="ticket" method="post" cssClass="validatedForm">

  <h3>Edit Ticket ${ticket.id} Details</h3>

  <table class="info_table_form">

    <tr>
      <c:if test="${ticket.type != 'CUSTOMER' && ticket.type != 'INQUIRY'}">
        <td><form:label path="creatorId">Creator</form:label></td>
        <td><input type="text" value="${creator.email}" readOnly /></td>
      </c:if>
    </tr>

    <c:if test="${ticket.type != 'INQUIRY'}">
      <tr>
        <td><form:label path="customerId">Customer</form:label></td>
        <td><input type="text" value="${customer.email}" readOnly />
      </tr>
      <tr>
        <td><form:label path="assigneeId">Assigned To</form:label></td>
        <td><form:select path="assigneeId" cssErrorClass="validationFailed">
            <form:option value="0">Select one</form:option>
            <form:options items="${INTERNAL_USERS}" itemLabel="email" itemValue="userId" />
          </form:select></td>
      </tr>
    </c:if>

    <tr>
      <td><form:label path="priority">Priority</form:label></td>
      <td><form:select path="priority" cssErrorClass="validationFailed">
          <form:options items="${priorityList}" itemLabel="description" />
        </form:select></td>
    </tr>

    <tr>
      <td><form:label path="category.id" cssClass="required">Ticket Category</form:label></td>
      <td><form:select path="category.id" cssErrorClass="validationFailed">
          <option value="0">Select one...</option>
          <c:forEach var="cat" items="${ticketCategories}">
            <optgroup label="${cat.description}">
              <form:options items="${cat.subcategories}" itemValue="id" itemLabel="description" />
            </optgroup>
          </c:forEach>
        </form:select></td>
    </tr>
  </table>

  <!-- Begin Description -->
  <table class="info_table_form">
    <tr>
      <td><form:label path="title">Title</form:label></td>
      <td><form:input path="title" cssErrorClass="validationFailed" /></td>
    </tr>
    <tr>
      <td><form:label path="status">Status</form:label></td>
      <td><form:select path="status" cssErrorClass="validationFailed">
          <form:options items="${statusList}" itemLabel="description" />
        </form:select></td>
    </tr>
    <tr>
      <td><form:label path="description">Description</form:label></td>
      <td><form:input path="description" cssErrorClass="validationFailed" /></td>
    </tr>
  </table>
  <!-- End Description -->

  <!-- Begin Dates -->
  <table class="info_table_form">
    <tr>
      <td><form:label path="createdDate">Creation Date</form:label></td>
      <td><input type="text" value="${ticket.createdDate}" readOnly /></td>
    </tr>
    <tr>
      <td><form:label path="lastModifiedDate">Last Modified Date</form:label></td>
      <td><input type="text" value="${ticket.lastModifiedDate}" readOnly /></td>
    </tr>
  </table>
  <!-- End Dates -->

  <div class="buttons">
    <button type="button" class="button" onclick="location.href='../view/${ticket.id}'">Cancel</button>
    <button type="submit" class="button">Save</button>
  </div>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>