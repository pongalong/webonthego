<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Admininstrator</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Users currently online</h3>
        <p>This is a list of users currently viewing their accounts as well as their active sessions, time of their last action, and whether
          their session has been invalidated. Clicking "logout" will force users out of their active session.</p>
        <div style="margin-left: 30px; font-size: 12px; color: gray;">
          <span>[User email/login] - logout]</span><br /> <span style="margin-left: 30px;">[session ID] - [Last activity] - [Session invalidated]</span>
        </div>
        <div>
          <ul>
            <c:forEach var="user" items="${activeUsers}" varStatus="status">
              <li style="margin-top: 10px;">${user.email} - <a href="<spring:url value="/admin/logout/${user.userId}" />">logout</a> <c:if
                  test="${!empty userSessionInfo[status.index]}">
                  <ul style="margin: 0px; padding: 0 0 0 30px;">
                    <c:forEach var="sessionInfo" items="${userSessionInfo[status.index]}">
                      <li style="font-size: 0.85em; margin: 0px;">${sessionInfo.sessionId} - ${sessionInfo.lastRequest} - ${sessionInfo.expired}</li>
                    </c:forEach>
                  </ul>
                </c:if>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>


      <div class="span-6 last sub-navigation">
        <%@ include file="/WEB-INF/views/include/admin/navigation/adminNav.jsp"%>
      </div>

    </div>

    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>