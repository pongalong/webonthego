<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>TruConnect Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
<script type="text/javascript" src="<spring:url value="/static/javascript/pages/highlight/navigation/devices.js" />"></script>

<c:if test="${!empty sessionScope.controlling_user}">
  <script type="text/javascript">
			$(function() {
				$(".device_esn").click(function() {
					var deviceDetails = $(this).parent().parent().parent(".device_name").parent(".device").children(".device_detail");
					$(deviceDetails).slideToggle();
				});
			});
		</script>
</c:if>


</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>



  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder" style="margin-top: 20px; min-height: 200px;">

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Manage Devices</h3>

        <c:forEach var="device" items="${devices}" varStatus="status">

          <div class="device" style="position: relative; padding: 20px 0 20px 0; margin: 20px 0 20px 0;">

            <div class="device_balance" style="position: absolute; top: 0; right: 0;">
              <div class="badge">
                Current Balance: $
                <fmt:formatNumber value="${device.account.balance}" pattern="0.00" />
              </div>
              <div style="text-align: center;">Top-Up Amount: $${device.topUp}</div>
            </div>

            <div class="device_name" style="position: absolute; top: 0; left: 0;">
              <div class="btn-group">
                <button class="btn dropdown-toggle" data-toggle="dropdown" style="min-width: 300px; text-align: left;">
                  <c:choose>
                    <c:when test="${device.deviceInfo.statusId != 2}">
                      <span style="color: #999;">${device.deviceInfo.label}</span>
                      : <span class="device_status" style="color: #A17474;">Inactive</span>
                    </c:when>
                    <c:otherwise>
                    ${device.deviceInfo.label}
                      <!-- no special text for active devices -->
                    </c:otherwise>
                  </c:choose>
                  <span class="caret" style="float: right;"></span>
                </button>
                <ul class="dropdown-menu" style="margin: 1px; padding: 0; min-width: 296px;">
                  <li style="padding: 6px 0px 0px 14px;" class="device_esn">ESN: ${device.deviceInfo.value}</li>
                  <li class="divider"></li>
                  <li><a href="<spring:url value="/devices/rename/${device.encodedDeviceId}" />">Rename</a></li>
                  <li style="margin-bottom: 10px;"><a href="<spring:url value="/devices/topUp/${device.encodedDeviceId}" />">Change Top-Up</a></li>
                </ul>
              </div>
            </div>

            <c:if test="${!empty sessionScope.controlling_user}">
              <%@ include file="/WEB-INF/views/include/admin/devices/deviceInfo.jsp"%>
            </c:if>
          </div>

          <c:if test="${fn:length(devices) > status.index + 1}">
            <div style="border-bottom: 1px #ddd solid"></div>
          </c:if>
        </c:forEach>

        <div class="buttons" style="margin-top: 10px; padding-top: 10px; border-top: 1px #ccc dotted;">
          <a href="<spring:url value="/activate" />" class="button action-m" style="float: right;"><span>Add New Device</span> </a>
        </div>

      </div>

      <div class="span-6 last sub-navigation">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/include/footer_links.jsp"%>
  </div>
</body>
</html>