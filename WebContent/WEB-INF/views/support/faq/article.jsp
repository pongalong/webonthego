<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Show Ticket Information</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder">
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

      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>


  </div>

  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>