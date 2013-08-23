<link rel="stylesheet" href="<spring:url value='/static/styles/admin/wotg_admin.css' />" type="text/css" />
<script type="text/javascript" src="<spring:url value='/static/javascript/admin/admin_controls.js' />"></script>

<div id="admin-control-container">

  <!-- ADMIN LOGO -->
  <div id="admin-logo-container">
    <a href="<spring:url value="/home" />"> <img src="<spring:url value='/static/images/logo/logo_admin_sm.png' />" />
    </a>
  </div>
  <!-- END ADMIN LOGO -->

  <!-- LOGOUT/ID -->
  <div id="admin-logout-container">
    <c:choose>
      <c:when test="${CONTROLLING_USER.userId != -1}">
        <b>Internal:</b>
        <c:out value="${CONTROLLING_USER.username}" />
      </c:when>
      <c:otherwise>
        <sec:authentication property="principal.authorities" />
        <sec:authentication property="principal.username" />
      </c:otherwise>
    </c:choose>
    <a href="<spring:url value='/logout' />">Logout</a>
  </div>
  <!--  END LOGOUT/ID -->

  <!-- SET CURRENTLY VIEWED USER -->
  <c:choose>
    <c:when test="${USER.userId > 0}">
      <c:set var="currentUser" value="${sessionScope.USER.userId} ${sessionScope.USER.email}" />
    </c:when>
    <c:otherwise>
      <c:set var="currentUser" value="Search by Email or ID" />
    </c:otherwise>
  </c:choose>
  <!--  END CURRENTLY VIEWED USER -->

  <!-- SEARCH FORM -->
  <form id="admin-search" method="post" action="<spring:url value="/search/user" />">

    <input type="text" name="admin-search-param-id" id="admin-search-param-id" class="hidden" value="${USER.userId}" />

    <div class="input-append">
      <input autocomplete="off" name="admin-search-param" id="admin-search-param" type="text" placeholder="${currentUser}" />
      <button id="admin-search-submit" class="btn" type="submit">Go</button>
      <button id="admin-search-reset" class="btn" type="reset">Reset</button>
    </div>

    <div id="admin-search-results-container">
      <div id="admin-search-results-header">
        <span class="id">ID</span><span class="value">Email</span>
      </div>
      <div id="admin-search-results"></div>
    </div>

  </form>
  <!--  END SEARCH FORM -->

</div>