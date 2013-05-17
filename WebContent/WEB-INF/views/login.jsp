<%@ include file="/WEB-INF/views/include/header.jsp"%>

<!--  Begin left column -->
<div class="span-15 colborder">

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

    <c:if test="${not empty param.login_error}">
      <div class="alert error" style="width: 335px; margin-top: 10px; padding-right: 0;">
        <span style="color: #666;">&#8226; Username or Password was incorrect.</span>
      </div>
    </c:if>

    <!--  Begin login box -->
    <div class="span-9">
      <input class="span-9" id="j_username" type="text" name="j_username" placeholder="Email Address" value="${last_username}" /> 
      <input class="span-9 hidden" id="j_password" type="password" name="j_password" /> 
      <input class="span-9" id="j_password_holder" type="text" name="j_password_holder" placeholder="Password" value="Password" /> 
      <a href="<spring:url value='/reset/password' />" style="float: left;">Lost password</a> 
      <input type="submit" value="Login" style="float: right;" />
    </div>
    <!--  End login box -->

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
  <a class="mBtn" href="<spring:url value="/register" />" style="float: right;">Create an Account</a>
</div>
<!--  End right column -->

<%@ include file="/WEB-INF/views/include/footer.jsp"%>