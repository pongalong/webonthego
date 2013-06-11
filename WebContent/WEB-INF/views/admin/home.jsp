<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Users currently online</h3>

<div class="alert alert-info">
  <p>This is a list of users currently viewing their accounts as well as their active sessions, time of their last action, and whether their session has
    been invalidated. Clicking "logout" will force users out of their active session.</p>
  <p style="margin-left: 30px; font-size: 12px; color: gray;">
    <span>[User email/login] - logout]</span><br /> <span style="margin-left: 30px;">[session ID] - [Last activity] - [Session invalidated]</span>
  </p>
</div>

<table>

  <c:forEach var="session" items="${activeSessions}">

    <tr>
      <td><a href="#" class="userSession">${session.user.email}</a>

        <div class="sessionInfo hidden">
          <ul>
            <c:forEach var="sessionInfo" items="${session.sessionInformation}">
              <li><span>${sessionInfo.sessionId}</span><span>${sessionInfo.lastRequest}</span><span>${sessionInfo.expired}</span></li>
            </c:forEach>
          </ul>
        </div></td>
      <td style="vertical-align: top;"><sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SU')">
          <span class="forceLogout hidden"> <a href="<spring:url value="/admin/session/logout/${session.user.userId}" />">force logout</a></span>
        </sec:authorize></td>

    </tr>

  </c:forEach>

</table>

<style type="text/css">
.sessionInfo ul li span {
	margin-left: 20px;
}

.sessionInfo ul li:first-child {
	color: red;
	margin-left: 0 !important;
}
</style>

<script type="text/javascript">
	$(function() {
		$(".userSession").click(function(e) {
			e.preventDefault();
			$(this).next(".sessionInfo").toggleClass("hidden");
			$(this).parent().next().children(".forceLogout").toggleClass("hidden");
		});
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>