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
    <div class="container">Manage Devices</div>
  </div>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">

        <form:form id="deactivateDevice" cssClass="validatedForm" method="POST" commandName="deviceInfo">

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
                  </c:forEach>
                </spring:bind>
              </div>
            </div>
          </c:if>

          <div class="notice">
            <h1>Attention!</h1>
            <p>You are about to deactivate a device and the associated account. The balance in this account will
              expire after 60 days.</p>
          </div>

          <h3>Deactivate Device</h3>
          <p>After you click "Deactivate Device" at the bottom of this page:
          <ul>
            <li>Your device (ESN: ${deviceInfo.value}) and the account "${deviceInfo.label}" will be deactivated
              immediately.</li>
            <li>If the device is currently connected, the service will stop after the current session.</li>
            <li>You will no longer be charged the monthly access fee for this account, the monthly access fee paid
              for this period expires on ${accessFeeDate.month}/${accessFeeDate.day}/${accessFeeDate.year}.</li>
            <li>If you reactivate this account before the current monthly access fee expires, you will be charged
              the next monthly access fee on ${accessFeeDate.month}/${accessFeeDate.day}/${accessFeeDate.year}.</li>
            <li>Your balance of $<fmt:formatNumber value="${account.balance}" pattern="0.00" /> will remain in your account for 60 days after deactivation. If
              you reactivate a device within 60 days, you can continue using your balance. After this period your
              balance will expire.</li>
          </ul>
          </p>

          <p>Are you sure you want to deactivate device ${deviceInfo.label}?</p>

          <div class="row" style="display: none;">
            <form:input path="id" />
            <form:input path="label" />
            <form:input path="value" />
          </div>

          <div class="buttons">
            <a class="button action-m" href="#" id="deactivateDeviceButton"><span>Deactivate</span> </a> <a
              class="button escape-m multi" href="<spring:url value="/devices" />"><span>Cancel</span> </a><input
              id="deactivateDeviceSubmit" type="submit" value="Deactivate" style="display: none;" />
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