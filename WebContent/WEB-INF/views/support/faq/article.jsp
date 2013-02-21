<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <c:if test="${!empty category}">
    <h1 style="border-bottom: 1px #ccc dotted;">Frequently Asked Questions : ${category.title}</h1>
  </c:if>

  <table>
    <c:if test="${empty articleList}">
      <h3>No Articles Available</h3>
    </c:if>
    <c:forEach var="article" items="${articleList}">
      <tr>
        <td><c:choose>
            <c:when test="${fn:length(articleList) > 1}">
              <h3>
                <a href="<spring:url value="/support/faq/article/${article.id}"/>">${article.subject}</a>
              </h3>
            </c:when>
            <c:otherwise>
              <h3>${article.subject}</h3>
            </c:otherwise>
          </c:choose></td>
      </tr>
      <tr>
        <td><p style="width: 650px;">${article.articleData.contentsText}</p></td>
      </tr>
    </c:forEach>
  </table>
  <div class="buttons" style="text-align: center;">
    <a href="<spring:url value="/support/faq" />" class="mBtn">Back to FAQ</a>
  </div>
</div>



<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>