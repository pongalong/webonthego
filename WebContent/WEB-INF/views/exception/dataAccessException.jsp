<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>

  <div class="container">
    <%@ include file="/WEB-INF/views/include/header.jsp"%>
  </div>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Information Currently Unavailable</h3>
        <p style="font-size: 1.3em;">An error has occured while handling your request. No changes were made. Please try your request again.</p>
      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>

    </div>
    <!-- Close main-content -->

  </div>
  <!-- Close container -->
  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>