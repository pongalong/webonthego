<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Please Enter Article Information</h3>

  <form:form id="insertArticle" cssClass="validatedForm" method="post" commandName="article">

    <div class="row">
      <form:label path="subject" cssClass="required">Subject</form:label>
      <form:input path="subject" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row">
      <form:label path="articleData.contents" cssClass="required">Contents</form:label>
      <form:input path="articleData.contents" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row">
      <form:label path="articleData.contentsText">ContentsText</form:label>
      <form:input path="articleData.contentsText" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
    </div>

    <div class="row">
      <form:label path="categories[0].id" cssClass="required">Category</form:label>
      <form:select path="categories[0].id" cssClass="span-8" cssErrorClass="span-8 validationFailed" cssStyle="width:312px;">
        <option value="0" selected="selected">Select one</option>
        <c:forEach var="category" items="${categoryList}" varStatus="status">
          <form:option value="${ccategories[0].id}">${category.id}--${category.title}</form:option>
        </c:forEach>
      </form:select>
    </div>

    <div class="buttons">
      <input type="submit" name="_eventId_submit" value="Submit" />
    </div>

  </form:form>
</div>
<!-- border -->

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>