<c:choose>
  <c:when test="${USER.userId > 0}">
    <h3>Manage Account</h3>
    <ul>
      <li><a href="<spring:url value="/account"/>">Overview</a></li>
      <li><a href="<spring:url value="/profile"/>">Profile</a></li>
      <li><a href="<spring:url value="/devices"/>">Devices</a></li>
      <li><a href="<spring:url value="/account/activity"/>">Activity</a></li>
      <li><a href="<spring:url value="/account/payment/history"/>">Payments</a></li>
      <li><a href="<spring:url value="/coupons"/>">Promotions</a></li>
    </ul>
  </c:when>
  <c:when test="${CONTROLLING_USER.userId > 0 && USER.userId <= 0}">
    <h3>Administration</h3>
    <ul>
      <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
        <li><a href="<spring:url value="/admin/users"/>">View Users</a></li>
        <li><a href="<spring:url value="/admin/create" />">Create New Agent</a></li>
        <li><a href="<spring:url value="/support/faq/create/article" />">Create Articles</a></li>
      </sec:authorize>
    </ul>
  </c:when>
  <c:otherwise>
    <h3>Manage Account</h3>
    <ul>
      <li><a href="<spring:url value="/login"/>">Login</a></li>
      <li><a href="<spring:url value="/register"/>">Register</a></li>
    </ul>
  </c:otherwise>
</c:choose>