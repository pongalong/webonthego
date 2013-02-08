<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder" style="min-height: 200px;">

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Tickets</h3>
        <p>You must select a user account before viewing tickets.</p>

      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/admin/navigation/adminNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>
    <!-- Close main-content -->

  </div>
  <!-- Close container -->
  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>