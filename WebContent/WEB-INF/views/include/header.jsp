<c:if test="${not empty CONTROLLING_USER}">
  <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER, ROLE_AGENT, ROLE_SU">
    <%@ include file="/WEB-INF/views/include/admin/control_bar.jsp"%>
  </sec:authorize>
</c:if>


<div id="rt-header">
  <div class="container" style="position: relative;">
    <ul class="menutop">
      <li class="mBtn" onclick="location.href='<spring:url value="/" />'">Home</li>
      <li class="mBtn" onclick="location.href='https://store.webonthego.com/'">Store</li>
      <li class="mBtn" onclick="location.href='<spring:url value="/support" />'">Support</li>
    </ul>

    <c:if test="${USER.userId > 0 && CONTROLLING_USER.userId <= 0}">
      <div style="position: absolute; right: 0; text-align: right;">
        Welcome ${USER.contactInfo.firstName} ${USER.contactInfo.lastName}<br /> <a href="<spring:url value='/logout' />">Logout</a>
      </div>
    </c:if>

  </div>
</div>


<div class="container">
  <div id="logo">
    <img src="<spring:url value="/static/images/logo.png" />" alt="Web on The Go" />
  </div>
</div>