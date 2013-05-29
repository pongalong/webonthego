<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>



<form:form commandName="accountDetail" method="post">
  <fieldset>
    <legend>Change Topup Amount</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="topUp" />
        <spring:bind path="accountDetail">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="alert alert-info">
      <p>When the balance for this device runs below $2, your credit card will be charged the topup amount selected below.</p>
      <p>${accountDetail.deviceInfo.label} (${accountDetail.deviceInfo.status})</p>
      Current Balance: $
      <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
    </div>

    <form:label path="topUp" cssClass="radio">
      <form:radiobutton path="topUp" value="10.00" />
      $10.00
    </form:label>
    <form:label path="topUp" cssClass="radio">
      <form:radiobutton path="topUp" value="20.00" />
      $20.00
    </form:label>
    <form:label path="topUp" cssClass="radio">
      <form:radiobutton path="topUp" value="30.00" />
      $30.00
    </form:label>


    <div>
      <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
      <button type="submit" class="button">Save</button>
    </div>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>