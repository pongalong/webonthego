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

  <div class="blueTruConnectGradient">
    <div class="container">Reinstall Device</div>
  </div>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Confirm</h3>

        <form:form id="reactivateDevice" cssClass="validatedForm" method="POST" commandName="deviceInfo">

          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.deviceInfo'].allErrors}">
            <div class="row">
              <div class="alert error">
                <h1>Please correct the following problems</h1>
                <form:errors path="id" />
                <form:errors path="value" />
                <form:errors path="label" />
                <spring:bind path="deviceInfo">
                  <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                    <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                    <br>
                  </c:forEach>
                </spring:bind>
              </div>
            </div>
          </c:if>

          <p>Are you sure you want to reactivate device ${deviceInfo.label}?</p>

          <div class="row" style="display: none;">
            <form:input path="id" />
            <form:input path="label" />
            <form:input path="value" />
          </div>

          <div class="buttons">
            <a class="button action-m" href="#" id="reactivateDeviceButton"><span>Reactivate</span> </a> <a
              class="button escape-m multi" href="<spring:url value="/devices" />"><span>Cancel</span> </a><input
              id="reactivateDeviceSubmit" type="submit" value="Deactivate" style="display: none;" />
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