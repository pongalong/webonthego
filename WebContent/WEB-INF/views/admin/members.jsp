<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3>${memberType}</h3>
  <div>
    <ul>
      <c:forEach var="member" items="${members}" varStatus="status">
        <li style="margin-top: 10px;">${member.username} - <c:choose>
            <c:when test="${member.enabled}">enabled (<a href="<spring:url value="/manager/toggle/${member.userId}?cmd=DISABLE" />">disable</a>)</c:when>
            <c:when test="${!member.enabled}">disabled (
                    <a href="<spring:url value="/manager/toggle/${member.userId}?cmd=ENABLE" />">enable</a>)</c:when>
          </c:choose>
        </li>
      </c:forEach>
    </ul>
  </div>
</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>