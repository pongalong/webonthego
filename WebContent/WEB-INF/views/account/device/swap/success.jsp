<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Success</h3>
  <p>You have successfully swapped your device's ESN.</p>
  <a class="mBtn" href="<spring:url value="/devices" />">Continue </a>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>