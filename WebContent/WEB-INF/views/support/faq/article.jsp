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
      <div class="row">
        <div class="span10">
          <c:choose>
            <c:when test="${fn:length(articleList) > 1}">
              <h3>
                <a href="<spring:url value="/support/faq/article/${article.id}"/>">${article.subject}</a>
              </h3>
            </c:when>
            <c:otherwise>
              <h3>${article.subject}</h3>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="alert">${article.articleData.contentsText}</div>

    </c:forEach>

  </c:otherwise>

</c:choose>

<div class="buttons">
  <a href="<spring:url value="/support/faq" />" class="mBtn">Back to FAQ</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>