<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Frequently Asked Questions</h3>

<table>
  <c:choose>
    <c:when test="${empty categoryList}">
      No Articles
    </c:when>
    <c:otherwise>
      <c:forEach var="category" items="${categoryList}">
        <tr>
          <td><a href="<spring:url value="/support/faq/category/${category.id}"/>"> ${category.title} (${category.totalArticles})</a></td>
        </tr>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</table>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>