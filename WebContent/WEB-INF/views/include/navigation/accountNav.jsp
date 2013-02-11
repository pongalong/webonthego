<c:choose>
  <c:when test="${USER.userId > 0}">
    <h3>Manage Account</h3>
    <ul>
      <li id="nav_overview"><a href="<spring:url value="/account"/>">Overview</a></li>
      <li id="nav_profile"><a href="<spring:url value="/profile"/>">Profile</a></li>
      <li id="nav_devices"><a href="<spring:url value="/devices"/>">Devices</a></li>
      <li id="nav_activity"><a href="<spring:url value="/account/activity"/>">Activity</a></li>
      <li id="nav_paymentHistory"><a href="<spring:url value="/account/payment/history"/>">Payments</a></li>
      <li id="nav_coupons"><a href="<spring:url value="/coupons"/>">Promotions</a></li>
    </ul>
  </c:when>
  <c:when test="${CONTROLLING_USER.userId > 0 && USER.userId <= 0}">
    <h3>Administration</h3>
    <ul>
      <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER, ROLE_SUPERUSER">
        <li id="nav_manageReps"><a href="<spring:url value="/servicerep/all"/>">View Service Agents</a></li>
      </sec:authorize>
      <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_SUPERUSER">
        <li id="nav_manageReps"><a href="<spring:url value="/manager/all"/>">View Managers</a></li>
        <li id="nav_manageAdmin"><a href="<spring:url value="/admin/all"/>">View Administrators</a></li>
        <li id="nav_createUser"><a href="<spring:url value="/admin/create" />">Create New Agent</a></li>
        <li id="nav_articles"><a href="<spring:url value="/support/faq/create/article" />">Create Articles</a></li>
      </sec:authorize>
    </ul>
  </c:when>
  <c:otherwise>
    <h3>Manage Account</h3>
    <ul>
      <li id="nav_overview"><a href="<spring:url value="/login"/>">Login</a></li>
      <li id="nav_overview"><a href="<spring:url value="/register"/>">Register</a></li>
    </ul>
  </c:otherwise>
</c:choose>