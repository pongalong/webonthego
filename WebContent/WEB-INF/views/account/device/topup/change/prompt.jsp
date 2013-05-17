<%@ include file="/WEB-INF/views/include/headerAndBody.jsp"%>

<h3>Change Topup Amount</h3>

<p>When the balance for this device runs below $2, your credit card will be charged the topup amount selected below.</p>

<div style="margin: 10px 0;">
  <h4>${accountDetail.deviceInfo.label} (${accountDetail.deviceInfo.status})</h4>
  <h4>
    Current Balance: $
    <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
  </h4>
</div>

<form:form id="topUp" commandName="accountDetail" method="POST" cssClass="validatedForm">

  <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
    <div class="row clearfix">
      <div class="alert error">
        <form:errors path="topUp" />
        <spring:bind path="accountDetail">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </div>
  </c:if>

  <form:radiobutton path="topUp" value="10.00" />$10.00<br />
  <form:radiobutton path="topUp" value="20.00" />$20.00<br />
  <form:radiobutton path="topUp" value="30.00" />$30.00<br />
  <div class="clear"></div>

  <div class="buttons">
    <a href="<spring:url value="/devices" />" class="mBtn">Cancel </a> <input type="submit" value="Update" />
  </div>
</form:form>

<%@ include file="/WEB-INF/views/include/footerAndNav.jsp"%>