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
    <div id="main-content">
      <h3>Your device has been activated!</h3>
      <p>You can now use your device.</p>

      <div class="buttons">
        <a href="<spring:url value='/' />" class="button action-m multi"><span>Home</span> </a>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>

</body>
</html>