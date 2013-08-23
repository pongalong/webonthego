<script type="text/javascript">
	$(function() {
		$(".device_esn").unbind('click').click(function() {
			var deviceDetails = $(this).parents(".device").children(".device_detail");
			$(deviceDetails).slideToggle();
		});
	});
</script>

<div class="clear"></div>
<div class="admin_tooltip device_detail" style="display: none; font-size: .9em;">
  <div>
    <li class="header">Device Information <a href="#" style="float: right;" onclick="$(this).parents('.device_detail').slideToggle(); return false;">close</a></li>
    <li>ESN: ${accountDetail.deviceInfo.value}</li>
    <li>Account Number: ${accountDetail.deviceInfo.accountNo}</li>
    <li>Device ID: ${accountDetail.deviceInfo.id}</li>
    <li>Status: ${accountDetail.deviceInfo.status}</li>
    <li>Status ID: ${accountDetail.deviceInfo.statusId}</li>
  </div>
  <div>
    <c:forEach var="package" items="${accountDetail.account.packageList}">
      <li class="header">Package Information</li>
      <li>Package ID: ${package.id}</li>
      <li>Package Name: ${package.name}</li>
      <c:if test="${!empty package.componentList}">
        <li class="header">Component Information</li>
        <c:forEach var="component" items="${package.componentList}">
          <li>Component ID: ${component.id}</li>
          <li>Component Name: ${component.name}</li>
        </c:forEach>
      </c:if>
    </c:forEach>
  </div>
  <div>
    <li class="header">Service Information</li>
    <c:forEach var="service" items="${accountDetail.account.serviceinstancelist}">
      <li>Subscriber Number: ${service.subscriberNumber}</li>
      <li>External ID: ${service.externalId}</li>
      <li>External ID Type: ${service.externalIdType}</li>
      <li>Active Date: ${service.activeDate}</li>
      <li>Inactive Date: ${service.inactiveDate}</li>
    </c:forEach>
  </div>
</div>