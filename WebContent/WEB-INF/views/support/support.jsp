<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <h3>Support</h3>

  <div style="border: 1px solid lightgrey; background-color: azure; padding: 5px; margin: 0 0 20px 0;">
    <h4>
      <a href="<spring:url value="/support/faq" />">Frequently Asked Questions</a>
    </h4>
  </div>

  <div style="border: 1px solid lightgrey; background-color: azure; padding: 5px; margin: 0 0 20px 0;">
    <h4>
      <a href="<spring:url value="/support/download" />">Download</a>
    </h4>
  </div>

  <div style="border: 1px solid lightgrey; background-color: azure; padding: 5px; margin: 20px 0 20px 0;">
    <h4>
      <c:choose>
        <c:when test="${user.userId > 0}">
          <a href="<spring:url value="/support/ticket/create"/>">Contact Us</a>
        </c:when>
        <c:otherwise>
          <a href="<spring:url value="/support/inquire"/>">Contact Us</a>
        </c:otherwise>
      </c:choose>
    </h4>
  </div>

  <form:form id="search" cssClass="search validatedForm" method="post" commandName="article">
    <h3 style="border-bottom: 1px #ccc dotted; margin-bottom: 10px;">Search</h3>

    <input id="search_support_input" type="text" name="keyword" placeholder="Enter your query here" style="width: 100%;"></input>

    <div class="clear"></div>
    <div class="buttons">
      <input type="submit" name="_eventId_submit"></input>
    </div>
  </form:form>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript">
	$(function() {
		$("#search_support_input").enableCaption();
	});
</script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>