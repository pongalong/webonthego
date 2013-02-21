<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Device Disconnected</h3>
  <p>You have successfully deactivated "${accountDetail.deviceInfo.label}".</p>
  <a class="button action-m" href="<spring:url value="/devices" />"><span>Continue</span> </a>
</div>

<div class="span-6 last sub-navigation formProgress">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>