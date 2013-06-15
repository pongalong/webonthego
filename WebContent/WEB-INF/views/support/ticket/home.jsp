<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<!-- BEGIN QUICKLINKS -->
<h3>Quick Links</h3>
<div class="row">
  <div class="span10">
    <a href="<spring:url value="/support/ticket/view/creator/me" />" class="span3 button">Tickets I Created</a> <a
      href="<spring:url value="/support/ticket/view/assignee/me" />" class="span3 button">Tickets Assigned to Me</a>
  </div>
</div>

<div class="row">
  <div class="span10">
    <a href="<spring:url value="/support/ticket/view/all" />" class="span3 button">All Tickets</a> <a href="<spring:url value="/support/ticket/view/inquiry" />"
      class="span3 button">All Inquiries</a>
  </div>
</div>
<!-- END QUICKLINKS -->

<!-- BEGIN INQUIRIES -->
<h3>Inquiries</h3>
<div class="row">
  <div class="span10">
    <a href="<spring:url value="/support/inquire/" />" class="span3 button">Open Inquiry</a>
  </div>
</div>
<!-- END INQUIRIES -->

<!-- BEGIN SEARCH -->
<h3>Search</h3>
<div class="row">
  <div class="span10">
    <a href="<spring:url value="/support/ticket/search" />" class="span3 button">Search</a>
  </div>
</div>
<!-- END SEARCH -->

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>