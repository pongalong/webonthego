<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3>Frequently Asked Questions</h3>

  <table border="1" cellspacing="10">
    <c:if test="${empty categoryList}">
      <c:out value="Category is empty!" />
    </c:if>
    <c:forEach var="category" items="${categoryList}">
      <tr>
        <td><a href="<spring:url value="/support/faq/category/${category.id}"/>"> ${category.title} (${category.totalArticles})</a></td>
      </tr>
    </c:forEach>
  </table>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>