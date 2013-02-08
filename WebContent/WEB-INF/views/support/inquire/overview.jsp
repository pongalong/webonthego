<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Customer Ticket Overview</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <c:set var="loggedinUserTicketsCount" value="0" scope="page" />

  <div class="container">
    <div class="mainbody">

      <div class="span-18 colborder" style="min-height: 200px;">
        <h3>Have A Question?</h3>
        <p>Send your question to us and we'll do our best to help you.</p>

        <a id="createTicket" href="<spring:url value="/support/inquire/create" />" class="button action-m"><span>Ask A Question</span></a>

      </div>
      <!-- span-18 -->
      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>

    <!-- close main-content -->

  </div>
  <!-- Close container -->

  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>