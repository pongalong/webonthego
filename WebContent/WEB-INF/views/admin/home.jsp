<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Users currently online</h3>

<div class="alert alert-info">
  <p>This is a list of users currently viewing their accounts as well as their active sessions, time of their last action, and whether their session has
    been invalidated. Clicking "logout" will force users out of their active session.</p>
  <p style="margin-left: 30px; font-size: 12px; color: gray;">
    <span>[User email/login] - logout]</span><br /> <span style="margin-left: 30px;">[session ID] - [Last activity] - [Session invalidated]</span>
  </p>
</div>

<c:forEach var="user" items="${activeUsers}" varStatus="status">

  <a href="#" class="userSession">${user.email}</a>
  <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SU')">
    <span class="forceLogout hidden"> <a href="<spring:url value="/admin/logout/${user.userId}" />">force logout</a></span>
  </sec:authorize>

  <div class="sessionInfo hidden">
    <ul>
      <c:forEach var="sessionInfo" items="${userSessionInfo[status.index]}">
        <li>[${sessionInfo.sessionId}] ${sessionInfo.lastRequest} ${sessionInfo.expired}</li>
      </c:forEach>
    </ul>
  </div>
</c:forEach>

<style type="text/css">
  .forceLogout {
    margin-left: 15px;
    color: black;
  }
</style>

<script type="text/javascript">
	$(function() {
		$(".userSession").click(function(e) {
			e.preventDefault();
			$(this).next(".forceLogout").toggleClass("hidden");
			$(this).next().next(".sessionInfo").toggleClass("hidden");
		});
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>