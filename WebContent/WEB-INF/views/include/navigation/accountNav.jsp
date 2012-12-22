<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
  <h3>Manage Account</h3>
  <ul>
    <li id="nav_overview"><a href="<spring:url value="/account"/>">Account Overview</a></li>
    <li id="nav_profile"><a href="<spring:url value="/profile"/>">Profile</a></li>
    <li id="nav_devices"><a href="<spring:url value="/devices"/>">Devices</a></li>
    <li id="nav_activity"><a href="<spring:url value="/account/activity"/>">Activity</a></li>
    <li id="nav_paymentHistory"><a href="<spring:url value="/account/payment/history"/>">Payments</a></li>
    <li id="nav_coupons"><a href="<spring:url value="/coupons"/>">Coupons</a></li>
  </ul>
</sec:authorize>
<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
  <h3>Manage Account</h3>
  <ul>
    <li id="nav_overview"><a href="<spring:url value="/login"/>">Login</a></li>
    <li id="nav_overview"><a href="<spring:url value="/account"/>">Register</a></li>
  </ul>
</sec:authorize>