<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Users (${requestedRole.name})</h3>

<c:choose>
  <c:when test="${not empty members}">

    <table>
      <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Enabled</th>
      </tr>

      <c:forEach var="member" items="${members}" varStatus="status">
        <tr>
          <td>${member.userId}</td>
          <td>${member.username}</td>
          <td>${member.email}</td>
          <td><c:choose>
              <c:when test="${member.enabled}">
                <a href="<spring:url value="/admin/user/toggle/${member.userId}" />" onclick="return confirm('Do you want to disable ${member.username}?')">
                  <img src="<spring:url value="/static/images/buttons/icons/accept.png" />" />
                </a>
              </c:when>
              <c:otherwise>
                <a href="<spring:url value="/admin/user/toggle/${member.userId}" />" onclick="return confirm('Do you want to enable ${member.username}?')">
                  <img src="<spring:url value="/static/images/buttons/icons/error.png" />" />
                </a>
              </c:otherwise>
            </c:choose></td>
        </tr>
      </c:forEach>
    </table>
  </c:when>
  <c:otherwise>
      No users.
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>