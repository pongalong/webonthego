<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Please Enter Article Information</h3>

<form:form id="insertArticle" cssClass="validatedForm" method="post" commandName="article">

  <div class="row clearfix">
    <form:label path="subject" cssClass="required">Subject</form:label>
    <form:input path="subject" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <form:label path="articleData.contents" cssClass="required">Contents</form:label>
    <form:input path="articleData.contents" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
    <form:label path="articleData.contentsText">ContentsText</form:label>
    <form:input path="articleData.contentsText" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
  </div>

  <div class="row clearfix">
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

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>