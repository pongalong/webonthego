<link rel="stylesheet" href="<spring:url value='/static/styles/admin/controlBar.css' />" type="text/css" />
<script type="text/javascript" src="<spring:url value='/static/javascript/pages/admin/controlBar.js' />"></script>

<div id="admin_control_bar">

  <!-- ADMIN LOGO -->
  <div class="logo">
    <a href="<spring:url value="/home" />"> <img src="<spring:url value='/static/images/logo/logo_admin_sm.png' />" />
    </a>
  </div>
  <!-- END ADMIN LOGO -->

  <!-- LOGOUT/ID -->
  <div class="logout">
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
  <div class="currentUser hidden">
    <a href="<spring:url value="/account" />">${currentUser} </a>
  </div>
  <!--  END CURRENTLY VIEWED USER -->

  <!-- SEARCH FORM -->
  <form id="adminControl" method="post" action="<spring:url value="/admin/search" />">

    <div style="float: left; padding-right: 5px;">
      <input name="admin_search_id" id="admin_search_id" type="text" class="hidden" value="${USER.userId}" /> <input autocomplete="off"
        name="admin_search_param" id="admin_search_param" type="text" placeholder="${currentUser}" />
      <div id="admin_search_results" class="search_results_box"></div>
    </div>

    <input id="adminControl_button_submit" type="submit" value="Go" class="mBtn" /> <input id="adminControl_button_reset" type="reset" class="mBtn" />

  </form>
  <!--  END SEARCH FORM -->

</div>