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
    <div class="mainbody">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Users currently online</h3>

        <div class="info">
          <p>This is a list of users currently viewing their accounts as well as their active sessions, time of their last action, and whether their session
            has been invalidated. Clicking "logout" will force users out of their active session.</p>
          <p style="margin-left: 30px; font-size: 12px; color: gray;">
            <span>[User email/login] - logout]</span><br /> <span style="margin-left: 30px;">[session ID] - [Last activity] - [Session invalidated]</span>
          </p>
        </div>

        <c:forEach var="user" items="${activeUsers}" varStatus="status">
          <div>
            <a href="#" onclick="$(this).parent().next().slideToggle();$(this).next().toggle();">${user.email}</a> <span style="display: none;"> - <a
              href="<spring:url value="/admin/logout/${user.userId}" />">force logout</a>
            </span>
          </div>
          <div style="display: none;">
            <ul>
              <c:forEach var="sessionInfo" items="${userSessionInfo[status.index]}">
                <li>[${sessionInfo.sessionId}] ${sessionInfo.lastRequest} ${sessionInfo.expired}</li>
              </c:forEach>
            </ul>
          </div>
        </c:forEach>
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