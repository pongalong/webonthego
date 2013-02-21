<%@ include file="/WEB-INF/views/include/header.jsp"%>

<!--  Begin left column -->
<div class="span-15 colborder" style="min-height: 250px;">
  <h3>Create Your Web on the Go &#8480; Account</h3>
  <p>If you have received your Web on the Go &#8480; device you can create your account and activate it here. Start by creating a new user account.</p>
  <p style="text-align: center; margin-top: 35px;">
    <a href='<spring:url value="/register" />' class="mBtn">Create Account</a>
  </p>
</div>
<!--  End left column -->

<!--  Begin right column -->
<div class="span-8 last">

  <form id="login" class="login" action="<spring:url value='/j_spring_security_check' />" method="post">
    <h3>Existing Users</h3>
    <p>If you already have a Web on the Go &#8480; login, you can manage your account, view your usage, adjust your payment options and add additional
      devices here.</p>

    <c:choose>
      <c:when test="${not empty param.login_error}">
        <c:set var="last_username" value="${SPRING_SECURITY_LAST_USERNAME}" />
      </c:when>
      <c:otherwise>
        <c:set var="last_username" value="" />
      </c:otherwise>
    </c:choose>

    <div>
      <input class="span-8" id="j_username" type="text" name="j_username" placeholder="Email Address" value="${last_username}" /> <input class="span-8 hidden"
        id="j_password" type="password" name="j_password" /> <input class="span-8" id="j_password_holder" type="text" name="j_password_holder"
        placeholder="Password" value="Password" />
    </div>

    <div>
      <input type="submit" value="Log In" style="float: right;" /> <a href="<spring:url value='/reset/password' />" style="float: left;">Forgot password</a>
    </div>

  </form>
</div>
<!--  End right column -->

<%@ include file="/WEB-INF/views/include/footer.jsp"%>