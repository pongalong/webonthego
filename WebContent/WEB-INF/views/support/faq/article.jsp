<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<c:if test="${!empty category}">
  <h3>Frequently Asked Questions : ${category.title}</h3>
</c:if>

<c:choose>

  <c:when test="${empty articleList}">
    <p>No Articles Available</p>
  </c:when>

  <c:otherwise>

    <c:forEach var="article" items="${articleList}">

      <h4>
        <c:choose>
          <c:when test="${fn:length(articleList) > 1}">
            <a href="<spring:url value="/support/faq/article/${article.id}"/>">${article.subject}</a>
          </c:when>
          <c:otherwise>
            ${article.subject}
          </c:otherwise>
        </c:choose>
      </h4>

      <div class="well">${article.articleData.contents}</div>

    </c:forEach>

  </c:otherwise>

</c:choose>

<div class="button-row">
  <a href="<spring:url value="/support/faq" />" class="button">Back to FAQ</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>