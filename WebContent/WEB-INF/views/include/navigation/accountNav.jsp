<c:if test="${USER.userId > 0}">

  <c:choose>
    <c:when test="${CONTROLLING_USER.userId > 0}">
      <h4 style="margin-bottom: 2px;">Manage User</h4>
      <div style="text-rendering: optimizeLegibility; font-size: .8em; margin-bottom: 8px;">${USER.email}</div>
    </c:when>
    <c:otherwise>
      <h4 style="margin-bottom: 0;">Manage Account</h4>
    </c:otherwise>
  </c:choose>

  <ul>
    <li><a href="<spring:url value="/account"/>">Overview</a></li>
    <li><a href="<spring:url value="/profile"/>">Profile</a></li>
    <li><a href="<spring:url value="/devices"/>">Devices</a></li>
    <li><a href="<spring:url value="/account/activity"/>">Activity</a></li>
    <li><a href="<spring:url value="/account/payment/history"/>">Payments</a></li>
    <li><a href="<spring:url value="/coupons"/>">Promotions</a></li>
  </ul>

</c:if>

<c:if test="${CONTROLLING_USER.userId > 0 && USER.userId > 0}">
  <h4>Support User</h4>
  <ul>
    <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_AGENT')">
      <li><a href="<spring:url value="/support/ticket/view/customer/${USER.userId}"/>">User Tickets</a></li>
      <li><a href="<spring:url value="/support/ticket/create/"/>">Create User Ticket</a></li>
    </sec:authorize>
  </ul>
</c:if>

<c:if test="${CONTROLLING_USER.userId > 0}">
  <h4>Administration</h4>
  <ul>
    <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
      <li><a href="<spring:url value="/admin/user/view"/>">View Users</a></li>
      <li><a href="<spring:url value="/admin/user/create" />">Create New User</a></li>
      <li><a href="<spring:url value="/support/faq/create/article" />">Create Articles</a></li>
    </sec:authorize>
    <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_AGENT')">
      <li><a href="<spring:url value="/support/ticket"/>">Tickets</a></li>
    </sec:authorize>
  </ul>
  <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SALES')">
    <h4>Affiliate</h4>
    <ul>
      <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN')">
        <li><a href="<spring:url value="/admin/affiliate" />">Affiliates</a>
      </sec:authorize>
      <li><a href="<spring:url value="/admin/affiliate/register/thirdparty"/>">Register New User</a></li>
    </ul>
  </sec:authorize>
</c:if>

<c:if test="${(empty CONTROLLING_USER && empty USER) || (CONTROLLING_USER.userId < 1 && USER.userId < 1)}">
  <h4>Manage Account</h4>
  <ul>
    <li><a href="<spring:url value="/login"/>">Login</a></li>
    <li><a href="<spring:url value="/register"/>">Register</a></li>
  </ul>
</c:if>