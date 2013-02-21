<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <form:form id="customerCreateTicket" cssClass="validatedForm" method="post" commandName="ticket">
    <h3>Send an Inquiry</h3>
    <p>Please provide as much information that is relevant to the issue as possible.</p>

    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticket'].allErrors}">
      <div class="row clearfix">
        <div class="alert error">
          <h1>Please correct the following problems</h1>
          <form:errors path="category" />
          <form:errors path="contactEmail" />
          <form:errors path="description" />
          <!-- Global Errors -->
          <spring:bind path="ticket">
            <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
              <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
            </c:forEach>
          </spring:bind>
        </div>
      </div>
    </c:if>

    <div class="row clearfix">
      <form:label path="contactEmail" cssClass="required">Email</form:label>
      <form:input path="contactEmail" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:label path="contactPhone">Phone</form:label>
      <form:input path="contactPhone" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row clearfix">
      <form:label path="category">Category</form:label>
      <form:select path="category" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
        <c:forEach var="category" items="${categoryList}" varStatus="status">
          <form:option value="${category}">${category.description}</form:option>
        </c:forEach>
      </form:select>
    </div>

    <div class="row clearfix" style="height: 250px;">
      <form:label path="description" cssClass="required">Description</form:label>
      <form:textarea path="description" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Submit" />
    </div>

  </form:form>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>


<%@ include file="/WEB-INF/views/include/footer.jsp"%>
