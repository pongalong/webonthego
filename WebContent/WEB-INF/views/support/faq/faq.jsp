<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Frequently Asked Questions</h3>

<table cellspacing="10">
  <c:if test="${empty categoryList}">
    <c:out value="Category is empty!" />
  </c:if>
  <c:forEach var="category" items="${categoryList}">
    <tr>
      <td><a href="<spring:url value="/support/faq/category/${category.id}"/>"> ${category.title} (${category.totalArticles})</a></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>