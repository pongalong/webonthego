<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Device Suspended</h3>
  <p>You have successfully suspended "${accountDetail.deviceInfo.label}".</p>
  <a class="button action-m" href="<spring:url value="/devices" />"><span>Continue</span> </a>
</div>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>