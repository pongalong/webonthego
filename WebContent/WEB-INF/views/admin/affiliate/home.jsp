<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Affiliate Source Codes</h3>

<c:choose>
  <c:when test="${not empty sourceCodeList}">

    <table>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Code</th>
      </tr>

      <c:forEach var="sc" items="${sourceCodeList}">
        <tr>
          <td><a href="<spring:url value="/admin/affiliate/source/update/${sc.id}" />">${sc.id}</a></td>
          <td><a href="<spring:url value="/admin/affiliate/source/update/${sc.id}" />">${sc.name}</a></td>
          <td>${sc.code}</td>
        </tr>
      </c:forEach>

    </table>
  </c:when>
  <c:otherwise>
    No source codes found.
    </c:otherwise>
</c:choose>

<div class="button-row align-right">
  <a href="<spring:url value="/admin/affiliate/source/add" />" class="button">Add</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>