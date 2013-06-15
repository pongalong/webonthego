<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form commandName="article" method="post" cssClass="form-horizontal">
  <fieldset>
    <legend>Create a New Article</legend>

    <!--Begin Error Display -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.article'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <!-- Global Errors -->
        <spring:bind path="article">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>
    <!--End Error Display -->

    <p>Create a new article that will appear in the FAQ sections.</p>

    <div class="control-group">
      <form:label path="subject" cssClass="control-label required">Subject</form:label>
      <div class="controls">
        <form:input path="subject" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="categories[0].id" cssClass="control-label required">Category</form:label>
      <div class="controls">
        <form:select path="categories[0].id" cssClass="span5" cssErrorClass="span5 validationFailed">
          <option value="0" selected="selected">Select one</option>
          <form:options items="${categoryList}" itemValue="id" itemLabel="title" />
        </form:select>
      </div>
    </div>

    <div class="control-group">
      <form:label path="articleData.contents" cssClass="control-label required">Content (HTML)</form:label>
      <div class="controls">
        <form:textarea path="articleData.contents" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="control-group">
      <form:label path="articleData.contentsText" cssClass="control-label required">Content (Text)</form:label>
      <div class="controls">
        <form:textarea path="articleData.contentsText" cssClass="span5" cssErrorClass="span5 validationFailed" />
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button">Create</button>
    </div>

  </fieldset>

</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>