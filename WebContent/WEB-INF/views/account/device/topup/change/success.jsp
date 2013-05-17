<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Change Topup Amount</h3>

<p>You have successfully changed your topup amount.</p>

<p>
  <strong>Device:</strong> ${accountDetail.deviceInfo.label}<br /> <strong>Topup:</strong> $${accountDetail.topUp}.
</p>

<p style="margin-top: 20px;">
  <a class="mBtn" href="<spring:url value="/devices" />">Continue</a>
</p>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>