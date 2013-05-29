<%@ include file="/WEB-INF/views/include/header/header.jsp"%>

<!--  Begin left column -->
<div class="span8 colborder">
  <h3>Create Your Web on the Go &#8480; Account</h3>
  <p>If you have received your Web on the Go &#8480; device you can create your account and activate it here. Start by creating a new user account.</p>
  <div style="text-align: center; margin-top: 35px;">
    <a href='<spring:url value="/register" />' class="button">Create Account</a>
  </div>
</div>
<!--  End left column -->

<!--  Begin right column -->
<div class="span4 colborderleft">

  <c:choose>
    <c:when test="${not empty param.login_error}">
      <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
    </c:when>
    <c:otherwise>
      <c:set var="last_username" value="" />
    </c:otherwise>
  </c:choose>

  <form id="login" class="login" action="<spring:url value='/j_spring_security_check' />" method="post">
    <fieldset>
      <legend>Existing Users</legend>

      <p>If you already have a Web on the Go &#8480; login, you can manage your account, view your usage, adjust your payment options and add additional
        devices here.</p>

      <input class="span4" id="j_username" type="text" name="j_username" placeholder="Email Address" value="${last_username}" /> <input class="span4"
        id="j_password" type="password" name="j_password" placeholder="Password">

      <div>
        <button class="button" type="submit" style="float: right;">Log In</button>
        <a href="<spring:url value='/reset/password' />" style="float: left;">Forgot password</a>
      </div>
    </fieldset>
  </form>

</div>
<!--  End right column -->

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>