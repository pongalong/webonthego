<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form:form method="POST" commandName="accountDetail">
  <fieldset>
    <legend>Disconnect Device</legend>

    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountDetail'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="deviceInfo.id" />
        <form:errors path="deviceInfo.value" />
        <form:errors path="deviceInfo.label" />
        <spring:bind path="accountDetail">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <div class="alert alert-info">
      <h4>Attention!</h4>
      <p>You are about to disconnect a device and the associated account. The balance in this account will expire after 60 days.</p>
      <p>After you click "Disconnect Device" at the bottom of this page:</p>
      <ul style="font-size: 14px; margin: 10px 50px;">
        <li>Your device (ESN: ${accountDetail.deviceInfo.value}) and the account "${accountDetail.deviceInfo.label}" will be disconnected immediately.</li>
        <li>If the device is currently connected, the service will stop after the current session.</li>
        <li>You will no longer be charged the monthly access fee for this account, the monthly access fee paid for this period expires on
          ${accessFeeDate.month}/${accessFeeDate.day}/${accessFeeDate.year}.</li>
        <li>If you reactivate this account before the current monthly access fee expires, you will be charged the next monthly access fee on
          ${accessFeeDate.month}/${accessFeeDate.day}/${accessFeeDate.year}.</li>
        <li>Your balance of $<fmt:formatNumber value="${account.balance}" pattern="0.00" /> will remain in your account for 60 days after deactivation. If
          you reactivate a device within 60 days, you can continue using your balance. After this period your balance will expire.
        </li>
      </ul>
    </div>

    <p>Are you sure you want to disconnect device ${accountDetail.deviceInfo.label}?</p>

    <button type="button" class="button" onclick="location.href='<spring:url value="/devices" />'">Cancel</button>
    <button type="submit" class="button">Disconnect</button>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>