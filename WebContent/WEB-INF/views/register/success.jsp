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
      <h3>Your account has been created!</h3>
      <p>You can activate your device now, or the next time you login.</p>

      <div class="buttons">
        <a href="<spring:url value='/activate' />" class="mBtn">Activate Now </a> <a href="<spring:url value='/' />" class="mBtn">Later </a>
      </div>

      <div class="clear"></div>
    </div>

  </div>
  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>