<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>User Created</h3>

  <p>
    ${newInternalUser.username}<br /> ${newInternalUser.email}<br /> 
    <c:forEach var="r" items="${newInternalUser.roles}">
      ${r.role.name}
    </c:forEach>
  </p>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>