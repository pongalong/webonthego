<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Support</h3>

<h4>
  <a href="<spring:url value="/support/faq" />">Frequently Asked Questions</a>
</h4>

<h4>
  <a href="<spring:url value="/support/download" />">Downloads</a>
</h4>

<h4>
  <c:choose>
    <c:when test="${USER.userId > 0}">
      <a href="<spring:url value="/support/ticket/create"/>">Contact Us</a>
    </c:when>
    <c:otherwise>
      <a href="<spring:url value="/support/inquire" />">Contact Us</a>
    </c:otherwise>
  </c:choose>
</h4>

<form method="post">

  <fieldset>
    <legend>Search</legend>

    <div class="input-append">
      <input type="text" class="span4" name="keyword" autocomplete="off" placeholder="Enter your query here" />
      <button type="submit" class="btn">Search</button>
    </div>

  </fieldset>

</form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>