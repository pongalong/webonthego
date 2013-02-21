<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <h3>Manage Devices</h3>

  <c:choose>
    <c:when test="${not empty ACCOUNT_DETAILS}">
      <c:forEach var="accountDetail" items="${ACCOUNT_DETAILS}" varStatus="status">

        <div class="device" style="position: relative; padding: 20px 0 20px 0; margin: 20px 0 20px 0;">

          <div class="device_balance" style="position: absolute; top: 0; right: 0;">
            <div class="badge">
              Current Balance: $
              <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
            </div>
            <div style="text-align: center;">Top-Up Amount: $${accountDetail.topUp}</div>
          </div>

          <div class="device_name" style="position: absolute; top: 0; left: 0;">
            <div class="btn-group">
              <button class="btn dropdown-toggle" data-toggle="dropdown" style="min-width: 300px; text-align: left;">
                <c:choose>
                  <c:when test="${accountDetail.deviceInfo.statusId != 2}">
                    <span style="color: #999;">${accountDetail.deviceInfo.label}</span>
                      : <span class="device_status" style="color: #A17474;">Inactive</span>
                  </c:when>
                  <c:otherwise>
                    ${accountDetail.deviceInfo.label}
                      <!-- no special text for active devices -->
                  </c:otherwise>
                </c:choose>
                <span class="caret" style="float: right;"></span>
              </button>
              <ul class="dropdown-menu" style="margin: 1px; padding: 0; min-width: 296px;">

                <c:choose>
                  <c:when test="${CONTROLLING_USER.userId > 0}">
                    <li style="padding: 6px 0px 0px 14px;">ESN: ${accountDetail.deviceInfo.value}</li>
                    <li class="device_esn"><a href="#">Show Details</a></li>
                  </c:when>
                  <c:otherwise>
                    <li style="padding: 6px 0px 0px 14px;">ESN: ${accountDetail.deviceInfo.value}</li>
                  </c:otherwise>
                </c:choose>


                <li class="divider"></li>
                <c:if test="${CONTROLLING_USER.userId > 0}">
                  <c:choose>
                    <c:when test="${accountDetail.deviceInfo.statusId == 2}">
                      <li><a href="<spring:url value="/devices/suspend/${accountDetail.encodedDeviceId}" />">Suspend</a></li>
                      <li><a href="<spring:url value="/devices/disconnect/${accountDetail.encodedDeviceId}" />">Disconnect</a></li>
                    </c:when>
                    <c:when test="${accountDetail.deviceInfo.statusId == 3}">
                      <li><a href="<spring:url value="/devices/reconnect/${accountDetail.encodedDeviceId}" />">Reconnect</a></li>
                    </c:when>
                    <c:when test="${accoutnDetail.deviceInfo.statusId == 5}">
                      <li><a href="<spring:url value="/devices/restore/${accountDetail.encodedDeviceId}" />">Restore</a></li>
                    </c:when>
                    <c:otherwise>
                      <li>Unknown Device Status</li>
                    </c:otherwise>
                  </c:choose>


                </c:if>
                <li><a href="<spring:url value="/devices/rename/${accountDetail.encodedDeviceId}" />">Rename</a></li>
                <li style="margin-bottom: 10px;"><a href="<spring:url value="/devices/topup/${accountDetail.encodedDeviceId}" />">Change Top-Up</a></li>
              </ul>
            </div>
          </div>

          <c:if test="${CONTROLLING_USER.userId > 0}">
            <%@ include file="/WEB-INF/views/include/admin/devices/deviceInfo.jsp"%>
          </c:if>

        </div>

        <c:if test="${fn:length(ACCOUNT_DETAILS) > status.index + 1}">
          <div style="border-bottom: 1px #ddd solid"></div>
        </c:if>

      </c:forEach>
    </c:when>
    <c:otherwise>
      <p>You have no devices.</p>
    </c:otherwise>
  </c:choose>

  <div class="buttons" style="margin-top: 10px; padding-top: 10px; border-top: 1px #ccc dotted;">
    <a href="<spring:url value="/activate" />" class="mBtn" style="float: right;">Add New Device </a>
  </div>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>