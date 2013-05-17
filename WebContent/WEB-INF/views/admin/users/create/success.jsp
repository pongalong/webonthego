<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>User Created</h3>

<p>
  ${newInternalUser.username}<br /> ${newInternalUser.email}<br />
  <c:forEach var="r" items="${newInternalUser.roles}">
      ${r.role.name}
    </c:forEach>
</p>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>