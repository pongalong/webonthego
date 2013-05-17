<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<c:if test="${!empty category}">
  <h1 style="border-bottom: 1px #ccc dotted;">Frequently Asked Questions : ${category.title}</h1>
</c:if>

<c:choose>
  <c:when test="${empty articleList}">
    <h3>No Articles Available</h3>
  </c:when>
  <c:otherwise>
    <table>
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
  </c:otherwise>
</c:choose>

<div class="buttons" style="text-align: center;">
  <a href="<spring:url value="/support/faq" />" class="mBtn">Back to FAQ</a>
</div>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>