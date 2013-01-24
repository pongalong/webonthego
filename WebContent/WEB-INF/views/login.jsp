<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/setupLoginForms.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">

      <!--  Begin left column -->
      <div class="span-15">

        <form id="login" class="login" action="<spring:url value='/j_spring_security_check' />" method="post">
          <h3>Login to Web on the Go &#8480;</h3>
          <p>If you already have a Web on the Go &#8480; account you can manage and add additional devices here.</p>

          <c:choose>
            <c:when test="${not empty param.login_error}">
              <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
            </c:when>
            <c:otherwise>
              <c:set var="last_username" value="" />
            </c:otherwise>
          </c:choose>

          <div class="span-9">
            <div style="height: 80px;">
              <input class="span-9" style="line-height: 22px;" id="j_username" type="text" name="j_username" title="Email Address" value="${last_username}" />
              <input class="span-9" style="line-height: 22px; display: none;" id="j_password" type="password" name="j_password" /> <input class="span-9"
                style="line-height: 22px;" id="j_password_holder" type="text" name="j_password_holder" title="Password" value="Password" />
            </div>

            <a id="login_button_submit" href="#" class="button action-m" style="float: right;"><span>Sign In</span> </a> <a
              href="<spring:url value='/retrieve/password' />" style="float: left;">Lost password</a> <input id="login_submit" type="submit" class="hidden" />
          </div>

        </form>

      </div>
      <!--  End left column -->

      <!--  Begin right column -->
      <div class="span-8 colborderleft last">
        <h3>Sign up for Web on the Go &#8480;</h3>
        <p>Don't have an account?</p>
        <ul style="height: 80px;">
          <li>Check usage</li>
          <li>Manage devices</li>
          <li>Get support</li>
        </ul>
        <a class="button action-m" href="<spring:url value="/register" />" style="float: right;"><span>Create an Account</span></a>
      </div>
      <!--  End right column -->

    </div>
    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>