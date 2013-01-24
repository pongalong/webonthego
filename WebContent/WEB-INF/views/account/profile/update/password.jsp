<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480;t Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>

</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder" style="min-height: 200px;">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Change Password</h3>

        <form:form id="updatePassword" method="POST" commandName="updatePassword" cssClass="validatedForm">
          <!--Begin Error Display -->
          <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.updatePassword'].allErrors}">
            <div class="row">
              <div class="alert error">
                <h1>Please correct the following problems</h1>
                <form:errors path="oldPassword" />
                <form:errors path="password" />
                <form:errors path="confirmPassword" />
              </div>
            </div>
          </c:if>
          <!--End Error Display -->

          <c:if test="${empty sessionScope.controlling_user}">
            <div class="row">
              <form:label path="oldPassword" cssClass="required">Old Password</form:label>
              <form:password path="oldPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
            </div>
          </c:if>

          <div class="row">
            <form:label path="password" cssClass="required">New Password</form:label>
            <form:password path="password" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <div class="row">
            <form:label path="confirmPassword" cssClass="required">Confirm Password</form:label>
            <form:password path="confirmPassword" cssClass="span-8" cssErrorClass="span-8 validationFailed" />
          </div>

          <!-- Buttons -->
          <div class="buttons">
            <a id="updatePassword_button_submit" href="#" class="button action-m"><span>Update Password</span> </a> <a href="<spring:url value="/profile" />"
              class="button escape-m multi"><span>Cancel</span> </a> <input id="updatePassword_submit" type="submit" value="Update Password" class="hidden" />
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