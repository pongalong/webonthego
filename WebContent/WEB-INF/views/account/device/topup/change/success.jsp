<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Change Topup Amount</h3>

<p>You have successfully changed your topup amount.</p>

<p>
  <strong>Device:</strong> ${accountDetail.deviceInfo.label}<br /> <strong>Topup:</strong> $${accountDetail.topUp}.
</p>

<p>
  <a class="button" href="<spring:url value="/devices" />">Continue</a>
</p>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>