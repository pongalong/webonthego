<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Create Ticket Successful</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder" style="min-height: 200px;">
        <h3>Ticket Submitted</h3>
        <p>We will investigate the issue and get back to you as soon as possible. Thank You!</p>
        <p>Ticket ID: ${ticket.id}</p>
        <a id="account" href="<spring:url value="/" />" class="button action-m"><span>Done</span></a>
      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>

  </div>

  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>