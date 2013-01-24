<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>TruConnect Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/setupForms.js" />"></script>
<script type="text/javascript" src="<spring:url value="/static/javascript/pages/highlight/navigation/devices.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Serial Number</h3>
        <form:form id="swapEsn" cssClass="validatedForm" method="post" commandName="deviceInfo">

          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.deviceInfo'].allErrors}">
            <div class="row">
              <div class="alert error">
                <h1>Please correct the following problems</h1>
                <form:errors path="value" />
                <form:errors path="label" />
                <spring:bind path="deviceInfo">
                  <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                    <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                  </c:forEach>
                </spring:bind>
              </div>
            </div>
          </c:if>

          <p>To swap your device we need to know the new serial number.</p>

          <!-- Esn -->
          <div class="row">
            <form:label cssClass="required" path="value">Serial Number (ESN)</form:label>
            <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="value" />
          </div>

          <div class="row">
            <form:label cssClass="required" path="label">Descriptive Name</form:label>
            <form:input cssClass="span-8" cssErrorClass="span-8 validationFailed" path="label" />
          </div>

          <!-- Buttons -->
          <div class="buttons">
            <a id="swapEsnButton" href="#" class="button action-m"><span>Continue</span> </a> <a href="<spring:url value="/devices" />"
              class="button escape-m multi"><span>Cancel</span> </a> <input id="swapEsnSubmit" type="submit" value="Continue" class="hidden" />
          </div>
        </form:form>
      </div>

      <div class="span-6 last sub-navigation formProgress">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/include/footer_links.jsp"%>
  </div>

</body>
</html>