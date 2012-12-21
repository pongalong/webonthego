<c:if test="${not empty sessionScope.controlling_user}">
  <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER">
    <%@ include file="/WEB-INF/views/include/admin/control_bar.jsp"%>
  </sec:authorize>
</c:if>

<div class="container">
  <div id="header">
    <!-- Begin Logo -->
    <div class="span-12">
      <div class="logo">
        <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
          <a href="http://www.truconnect.com/"> <img src="<spring:url value='/static/images/logo_s1.jpg' />" alt="TruConnect Logo" />
          </a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
          <img src="<spring:url value='/static/images/logo_s1.jpg'/>" alt="TruConnect Logo" />
        </sec:authorize>
      </div>
    </div>
    <!-- End Logo -->

    <div class="span-12 last">
      <!-- Begin Secondary Navigation -->
      <c:if test="${empty sessionScope.controlling_user}">
        <div class="secondary-navigation">
          <ul>
            <c:if test="${(empty sessionScope.admin) && (empty sessionScope.manager)}">
              <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
                <li>Welcome ${user.contactInfo.firstName} ${user.contactInfo.lastName}</li>
              </sec:authorize>
              <c:if test="${empty param.login_error}">
                <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
                  <li><a href="<spring:url value='/logout' />">Logout</a></li>
                </sec:authorize>
              </c:if>
            </c:if>
          </ul>
        </div>
      </c:if>
      <!-- End Secondary Navigation -->

      <!-- Begin Navigation -->
      <div class="navigation">
        <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
          <ul>
            <li><a href="http://www.truconnect.com/">Home</a></li>
            <li><a href="http://www.truconnect.com/plans/">Plans</a></li>
            <li><a href="https://store.truconnect.com/">Devices</a></li>
            <li><a href="/TruConnect/support/">Support</a></li>
          </ul>
        </sec:authorize>
      </div>
    </div>
    <!-- End Navigation -->
  </div>
</div>
<div class="clear"></div>

<div class="blueTruConnectGradient"></div>