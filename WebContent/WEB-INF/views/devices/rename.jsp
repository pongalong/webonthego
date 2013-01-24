<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/pages/highlight/navigation/devices.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">

        <form:form id="renameDevice" cssClass="validatedForm" method="post" commandName="accountDetail.deviceInfo">

          <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Rename Your Device</h3>

          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
            <div class="row">
              <div class="alert error">
                <h1>Please correct the following problems</h1>
                <form:errors path="label" />
                <spring:bind path="accountDetail.deviceInfo">
                  <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                    <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                  </c:forEach>
                </spring:bind>
              </div>
            </div>
          </c:if>

          <p>Enter the new name of your device.</p>

          <div class="row">
            <form:label cssClass="required" path="label">Descriptive Name</form:label>
            <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="label" />
          </div>

          <!-- Buttons -->
          <div class="buttons">
            <a id="renameDevice_button_submit" href="#" class="button action-m"><span>Rename</span> </a> <a href="<spring:url value="/devices" />"
              class="button escape-m multi"><span>Cancel</span> </a> <input id="renameDevice_submit" type="submit" value="Continue" class="hidden"></input>
          </div>
        </form:form>

      </div>

      <div class="span-6 last sub-navigation formProgress">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>

</body>
</html>