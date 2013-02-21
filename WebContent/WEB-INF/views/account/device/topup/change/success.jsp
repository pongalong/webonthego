
<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <h3>Change Top-Up Amount</h3>
  
  <p>You have successfully changed your top-up amount.</p>
  
  <p>
    <strong>Device:</strong> ${accountDetail.deviceInfo.label}<br /> <strong>Top-Up:</strong> $${accountDetail.topUp}.
  </p>

  <p style="margin-top: 20px;">
    <a class="mBtn" href="<spring:url value="/devices" />"><span>Continue</span> </a>
  </p>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>