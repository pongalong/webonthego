<%@ include file="/WEB-INF/views/include/header.jsp"%>

<c:set var="loggedinUserTicketsCount" value="0" scope="page" />

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Have A Question?</h3>
  <p>Send your question to us and we'll do our best to help you.</p>

  <a href="<spring:url value="/support/inquire/create" />" class="mBtn">Ask A Question</a>

</div>
<!-- span-18 -->
<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>