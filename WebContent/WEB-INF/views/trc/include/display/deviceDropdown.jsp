<!-- DEVICE DETAILS -->
<h1>Device Details</h1>
<li style="margin-left: 20px;">ESN: ${accountDetail.deviceInfo.value}</li>
<li class="device_esn"><a href="#" onclick="return false;">Show Details</a></li>

<li class="divider"></li>

<!-- USER FUNCTIONS -->
<c:if test="${CONTROLLING_USER.userId > 0}">
  <h1>User Functions</h1>
</c:if>
<li><a href="<spring:url value="/devices/rename/${accountDetail.encodedDeviceId}" />">Rename</a></li>
<li><a href="<spring:url value="/devices/topup/${accountDetail.encodedDeviceId}" />">Change Top-Up</a></li>

<c:if test="${CONTROLLING_USER.userId > 0}">
  <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_AGENT')">

    <li class="divider"></li>

    <!-- ADMIN PAYMENT FUNCTIONS -->
    <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
      <h1>Payment</h1>
      <li><a href="<spring:url value="/admin/payment/topup/queue/" />">Queue Topup</a></li>
      <li><a href="<spring:url value="/admin/payment/topup/force/${accountDetail.encodedDeviceId}" />">Force Topup</a></li>
      <li class="divider"></li>
    </sec:authorize>

    <!-- ADMIN NETWORK FUNCTIONS -->
    <h1>Network</h1>
    <c:choose>

      <c:when test="${accountDetail.deviceInfo.statusId == 2}">
        <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
          <li><a href="<spring:url value="/devices/suspend/${accountDetail.encodedDeviceId}" />">Suspend</a></li>
        </sec:authorize>
        <li><a href="<spring:url value="/devices/disconnect/${accountDetail.encodedDeviceId}" />">Disconnect</a></li>
      </c:when>

      <c:when test="${accountDetail.deviceInfo.statusId == 3}">
        <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
          <li><a href="<spring:url value="/devices/reconnect/${accountDetail.encodedDeviceId}" />">Reconnect</a></li>
        </sec:authorize>
      </c:when>

      <c:when test="${accountDetail.deviceInfo.statusId == 5}">
        <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN', 'ROLE_MANAGER')">
          <li><a href="<spring:url value="/devices/restore/${accountDetail.encodedDeviceId}" />">Restore</a></li>
        </sec:authorize>
      </c:when>

      <c:otherwise>
        <li><a href="#">Status: ${accountDetail.deviceInfo.status}</a></li>
      </c:otherwise>

    </c:choose>

  </sec:authorize>

  <!-- ADMIN ONLY FUNCTIONS -->
  <sec:authorize access="hasAnyRole('ROLE_SU', 'ROLE_ADMIN',)">
    <li><a href="<spring:url value="/devices/swap/${accountDetail.encodedDeviceId}" />">Swap</a></li>
  </sec:authorize>

</c:if>