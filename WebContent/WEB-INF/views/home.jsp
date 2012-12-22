<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web On The Go</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/setupLoginForms.js" />"></script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">

      <!--  Begin left column -->
      <div class="span-14">
        <h3>New Users</h3>
        <p>If you have received your WebOnTheGo device you can activate it here. Start by creating a new user account.</p>
        <div class="centerFloats">
          <ul>
            <li><a class="button action-l" href="<spring:url value="/register" />"><span>Create Account</span></a></li>
          </ul>
        </div>
      </div>
      <!--  End left column -->

      <!--  Begin right column -->
      <div class="span-9 colborderleft last">
        <h3>Existing Users</h3>
        <p>If you already have a WebOnTheGo account you can manage your devices here. Sign in to get started.</p>

        <c:choose>
          <c:when test="${not empty param.login_error}">
            <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
          </c:when>
          <c:otherwise>
            <c:set var="last_username" value="" />
          </c:otherwise>
        </c:choose>

        <form id="loginForm" action="<spring:url value='/j_spring_security_check' />" method="post">
          <div>
            <input class="span-9" style="line-height: 22px;" id="j_username" type="text" name="j_username" title="Email Address" value="${last_username}" />
            <input class="span-9" style="line-height: 22px; display: none;" id="j_password" type="password" name="j_password" />
            <input class="span-9" style="line-height: 22px;" id="j_password_holder" type="text" name="j_password_holder" title="Password" value="Password" />
          </div>
          <div>
            <a id="loginForm_button_submit" href="#" class="button action-m" style="float: right;"><span>Sign In</span> </a> 
            <input id="loginForm_submit" type="submit" class="hidden" />
            <a href="<spring:url value='/retrieve/password' />" style="float: left;">Lost password</a>
          </div>
        </form>

      </div>
      <!--  End right column -->

    </div>
    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>