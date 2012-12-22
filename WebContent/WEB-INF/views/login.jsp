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


      
        <form class="login span-10" action="<spring:url value='/j_spring_security_check' />" method="post">

          <fieldset>
            <legend>Login</legend>
            
             <c:choose>
              <c:when test="${not empty param.login_error}">
                <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
                <div class="alert error">
                  <h1>There was a problem logging in</h1>
                  <span style="color: #666;">&#8226; Username or Password was incorrect.</span>
                </div>
                <div class="clear" />
              </c:when>
              <c:otherwise>
                <c:set var="last_username" value="" />
              </c:otherwise>
            </c:choose>
          
            <input class="span-10" id="j_username" type="text" name="j_username" title="Email Address" value="${last_username}" />
            <input class="span-10" id="j_password_holder" type="text" name="j_password_holder" title="Password" value="Password" /> 
            <input class="span-10 hidden" id="j_password" type="password" name="j_password" />             
       
            <a id="loginForm_button_submit" href="#" class="button action-m" style="float: right;"><span>Sign In</span> </a> 
            <input id="loginForm_submit" type="submit" class="hidden" />
            
            <a href="<spring:url value='/retrieve/password' />" >Forgot password</a> | <a href="<spring:url value="/register" />" >Register</a>
            
         </fieldset>

        </form>
        



    </div>
    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>