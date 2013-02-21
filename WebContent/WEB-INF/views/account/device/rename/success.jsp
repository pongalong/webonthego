<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Device Renamed</h3>
  <p>Your device "${oldLabel}" has been renamed to "${newLabel}".</p>
  <p style="margin: 10px 0;">
    <a class="mBtn" href="<spring:url value="/devices" />">Continue </a>
  </p>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>