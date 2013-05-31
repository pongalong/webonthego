<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="ticket" method="post" cssClass="form-horizontal">
  <fieldset>
    <legend>Ask us a question before you activate</legend>

    <!-- Error Alert -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.ticket'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="category.id" />
        <form:errors path="contactEmail" />
        <form:errors path="description" />
        <!-- Global Errors -->
        <spring:bind path="ticket">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="well">
      <p>Send your question to us and we'll do our best to help you. Please provide as much information that is relevant to the issue as possible.</p>
    </div>

    <div class="control-group">
      <form:label path="contactEmail" cssClass="control-label required">Email</form:label>
      <div class="controls">
        <form:input path="contactEmail" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="contactPhone" cssClass="control-label">Phone</form:label>
      <div class="controls">
        <form:input path="contactPhone" placeholder="A Contact Number For Faster Response" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="category.id" cssClass="control-label required">Ticket Category</form:label>
      <div class="controls">
        <form:select path="category.id" cssClass="span5" cssErrorClass="span5 validationFailed">
          <form:option value="0">
            <spring:message code="label.selectOne" />
          </form:option>
          <c:forEach var="cat" items="${ticketCategories}">
            <optgroup label="${cat.description}">
              <c:forEach var="subcategory" items="${cat.subcategories}">
                <form:option value="${subcategory.id}">${subcategory.description}</form:option>
              </c:forEach>
            </optgroup>
          </c:forEach>
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="description" cssClass="control-label required">Description</form:label>
      <div class="controls">
        <form:textarea path="description" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button">Submit</button>
    </div>

  </fieldset>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>
