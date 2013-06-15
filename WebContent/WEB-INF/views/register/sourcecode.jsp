<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<style type="text/css">

/************************************************
* admin search controls
*************************************************/
#sourcecode-search-param {
	margin-top: 0px;
	margin-bottom: 0px;
	padding-left: 3px;
}

#sourcecode-search-results-container {
	z-index: 10;
	position: relative;
	overflow-x: hidden;
	overflow-y: auto;
	display: none;
	border: #ccc solid;
	border-width: 0px 1px 1px 1px;
	margin-top: 0;
	margin-left: 0;
	-moz-box-shadow: 5px 5px 5px #ccc;
	-webkit-box-shadow: 5px 5px 5px #ccc;
	box-shadow: 0px 5px 5px #ccc;
	background-color: rgba(255, 255, 255, 0.8);
	-moz-box-shadow: 5px 5px 5px #ccc;
}

#sourcecode-search-results-container .loading-msg {
	text-align: center;
	position: relative;
	top: 30px;
}

#sourcecode-search-results-container .results-list {
	list-style: none;
	margin: 0px;
	padding: 0px;
	width: 150%;
	list-style: none;
}

#sourcecode-search-results-container .id {
	float: left;
	text-align: right;
	width: 50px;
	padding-right: 10px;
}

#sourcecode-search-results-container .value {
	padding-left: 10px;
    float: left;
    width: 100px;
}

#sourcecode-search-results-container .desc {
	padding-left: 10px;
}

#sourcecode-search-results-header {
	border-bottom: 1px solid #ccc;
	background: #6E9C9A;
	color: white;
}

#sourcecode-search-results-header {
	border-bottom: 1px solid #ccc;
}

#sourcecode-search-results-header .id {
	border-right: 1px solid #ccc;
}

#sourcecode-search-results-header .value {
  border-right: 1px solid #ccc;
}

</style>

<form class="form-horizontal">
  <fieldset>
    <legend>Source Code</legend>

    <div class="well">
      <p>Enter the source code affiliated with one of our partners.</p>
      <p>This account is being created by ${CONTROLLING_USER.email}</p>
    </div>

    <div class="control-group">
      <label class="control-label required">Code</label>
      <div class="controls">
        <input autocomplete="off" name="sourcecode-search-param" id="sourcecode-search-param" type="text" class="span6" placeholder="Source Code" />
        <div id="sourcecode-search-results-container" class="span6">
          <div id="sourcecode-search-results-header">
            <span class="id">ID</span><span class="value">Code</span><span class="desc">Name</span>
          </div>
          <div id="sourcecode-search-results"></div>
        </div>
      </div>
    </div>
  </fieldset>
</form>

<form:form commandName="sourceCode" method="post" cssClass="form-horizontal">

  <fieldset>
    <!-- Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.sourceCode'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>

        <h4>Please correct the following problems</h4>
        <form:errors path="id" />
        <form:errors path="name" />
        <form:errors path="code" />
        <!-- Global Errors -->
        <spring:bind path="sourceCode">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="control-group hidden">
      <form:label path="id" cssClass="control-label required">ID</form:label>
      <div class="controls">
        <form:input path="id" cssClass="span6 numOnly" cssErrorClass="span6 validationFailed numOnly" />
      </div>
    </div>

    <div class="control-group hidden">
      <form:label path="code" cssClass="control-label required">Code</form:label>
      <div class="controls">
        <form:input path="code" cssClass="span6" cssErrorClass="span6 validationFailed" value="blank" />
      </div>
    </div>

    <div class="control-group hidden">
      <form:label path="name" cssClass="control-label">Name</form:label>
      <div class="controls">
        <form:input path="name" value="blank" cssClass="span6" cssErrorClass="span6 validationFailed" />
      </div>
    </div>

    <!-- Buttons -->
    <div class="controls">
      <button type="submit" class="button" name="_eventId_submit">Continue</button>
      <button type="button" class="button hidden" id="sourcecode-search-select-button">Select</button>
    </div>

  </fieldset>
</form:form>



<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>