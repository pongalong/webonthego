<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/default/include/header/headerAndMenu.jsp"%>

<h3>Manage Devices</h3>
deviceosa!
<c:choose>
  <c:when test="${not empty accountDetailCollection}">

    <c:forEach var="accountDetail" items="${accountDetailCollection}" varStatus="status">

      <div class="row">
        <div class="span10">

          <div class="device" style="position: relative; padding: 20px 0 20px 0; margin: 20px 0 20px 0;">

            <!-- Begin Balance and Topup -->
            <div class="device_balance" style="position: absolute; top: 0; right: 0;">
              <div class="badge">
                Current Balance: $
                <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
              </div>
              <div style="text-align: center;">Top-Up Amount: $${accountDetail.topUp}</div>
            </div>
            <!-- End Balance and Topup -->

            <!-- Begin Device and Dropdown -->
            <div class="device_name" style="position: absolute; top: 0; left: 0;">

              <!-- Begin Toggle Label -->
              <button class="btn dropdown-toggle" id="dLabel${status.index}" role="button" data-toggle="dropdown" data-target="#"
                style="min-width: 300px; text-align: left;">
                <c:choose>
                  <c:when test="${accountDetail.deviceInfo.statusId == 2}">
                    <span>${accountDetail.deviceInfo.label}:<span class="device_status fadedRed">Active</span></span>
                  </c:when>
                  <c:when test="${accountDetail.deviceInfo.statusId == 3}">
                    <span class="grey">${accountDetail.deviceInfo.label}:<span class="device_status fadedRed">Disconnected</span></span>
                  </c:when>
                  <c:when test="${accountDetail.deviceInfo.statusId == 5 }">
                    <span class="grey">${accountDetail.deviceInfo.label}:<span class="device_status fadedRed">Suspended</span></span>
                  </c:when>
                  <c:otherwise>
                    <span class="grey">${accountDetail.deviceInfo.label}:<span class="device_status fadedRed">${accountDetail.deviceInfo.status}</span></span>
                  </c:otherwise>
                </c:choose>
                <b class="caret" style="float: right;"></b>
              </button>
              <!-- End Toggle Label -->

              <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel${status.index}" style="margin: 1px; padding: 0; min-width: 296px;">
                <%@ include file="/WEB-INF/views/default/include/display/deviceDropdown.jsp"%>
              </ul>

            </div>
            <!-- End Device and Dropdown -->

            <c:if test="${CONTROLLING_USER.userId > 0}">
              <%@ include file="/WEB-INF/views/default/include/admin/devices/deviceInfo.jsp"%>
            </c:if>

          </div>

          <c:if test="${fn:length(AccountDetailCollection) > status.index + 1}">
            <div style="border-bottom: 1px #ddd solid"></div>
          </c:if>
        </div>
      </div>

    </c:forEach>
  </c:when>
  <c:otherwise>
    <p>You have no devices.</p>
  </c:otherwise>
</c:choose>

<h3></h3>
<a href="<spring:url value="/activate" />" class="button" style="float: right;">Add New Device </a>

<%@ include file="/WEB-INF/views/default/include/footer/footerAndMenu.jsp"%>