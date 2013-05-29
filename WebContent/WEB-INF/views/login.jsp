<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<!--  Begin left column -->
<div class="span8 colborder">

  <c:choose>
    <c:when test="${not empty param.login_error}">
      <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
    </c:when>
    <c:otherwise>
      <c:set var="last_username" value="" />
    </c:otherwise>
  </c:choose>

  <form id="login" class="login" action="<spring:url value='/j_spring_security_check' />" method="post">
    <h3>Login to Web on the Go &#8480;</h3>
    <p>If you already have a Web on the Go &#8480; account you can manage and add additional devices here.</p>

    <!--  Begin login box -->
    <div class="span5 offset1">
      <c:if test="${not empty param.login_error}">
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <li>Username or Password was incorrect.</li>
        </div>
      </c:if>

      <input class="span5" id="j_username" type="text" name="j_username" placeholder="Email Address" value="${last_username}" /> <input class="span5"
        id="j_password" type="password" name="j_password" placeholder="Password" /> <a href="<spring:url value='/reset/password' />" style="float: left;">Lost
        password</a>
      <button type="submit" class="button" style="float: right;">Login</button>
    </div>
    <!--  End login box -->

  </form>

</div>
<!--  End left column -->

<!--  Begin right column -->
<div class="span4 colborderleft">
  <h3>Sign up for Web on the Go &#8480;</h3>
  <p>Don't have an account?</p>
  <ul class="menu">
    <li>Check usage</li>
    <li>Manage devices</li>
    <li>Get support</li>
  </ul>
  <div style="text-align: right; margin-top: 20px;">
    <a class="button slim" href="<spring:url value="/register" />">Create an Account</a>
  </div>
</div>
<!--  End right column -->

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>