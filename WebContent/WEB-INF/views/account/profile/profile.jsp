<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">

  <c:if test="${not empty param.notification_sent}">
    <div class="info">
      <h1>An email has been sent to you</h1>
      <p>An email has been sent to ${param.notification_sent} with further instructions</p>
    </div>
  </c:if>

  <c:if test="${param.updated == 'password'}">
    <div class="success">
      <p>Your password has been successfully updated!</p>
    </div>
  </c:if>

  <c:if test="${param.updated == 'email'}">
    <div class="success">
      <p>Your email has been successfully updated!</p>
    </div>
  </c:if>

  <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Login Information</h3>
  <div>
    <sec:authorize ifAnyGranted="ROLE_SUPERUSER, ROLE_ADMIN, ROLE_MANAGER">
      <p style="line-height: 36px;">
        <span class="span-6">Status:</span>
        <c:choose>
          <c:when test="${USER.enabled}">
            <span class="span-8"> Enabled </span>
            <a href="<spring:url value="/profile/user/disable"/>" class="button escape-s" style="float: right;"
              onclick="return confirm('Do you want to disable ${USER.email}?')"><span>Disable</span> </a>
          </c:when>
          <c:otherwise>
            <span class="span-8">Disabled</span>
            <a href="<spring:url value="/profile/user/enable"/>" class="button escape-s" style="float: right;"
              onclick="return confirm('Do you want to enable ${USER.email}?')"><span>Enable</span> </a>
          </c:otherwise>
        </c:choose>
      </p>
    </sec:authorize>
    <p style="line-height: 36px; position: relative;">
      <span class="span-6">E-Mail Address:</span> <span class="span-8">${USER.email}</span> <a href="<spring:url value="/profile/update/email"/>"
        class="button semi-s" style="float: right;"><span>Change</span> </a>
    </p>
    <p style="line-height: 36px;">
      <span class="span-6">Password:</span> <span class="span-8">&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</span> <a
        href="<spring:url value="/profile/update/password"/>" class="button semi-s" style="float: right;"><span>Change</span> </a>
    </p>
  </div>

  <div class="clear"></div>


  <h3 style="margin: 10px 0 10px 0; border-bottom: 1px #ccc dotted;">Credit Cards</h3>

  <c:choose>
    <c:when test="${not empty PAYMENT_METHODS}">
      <c:forEach var="creditCard" items="${PAYMENT_METHODS}" varStatus="status">
        <div class="address dontsplit">
          <div class="btn-group">
            <button class="btn-discreet dropdown-toggle-discreet" data-toggle="dropdown"
              style="background: transparent; border-width: 0px; border-bottom: 1px solid #ddd; margin: 0; padding: 0; text-align: left; width: 180px; position: relative;">
              <c:choose>
                <c:when test="${creditCard.isDefault == 'Y'}">
                  <div style="font-weight: bold;">${creditCard.creditCardNumber}</div>
                </c:when>
                <c:otherwise>
                  <div>${creditCard.creditCardNumber}</div>
                </c:otherwise>
              </c:choose>
              <span class="caret" style="position: absolute; top: 8px; right: 0;"></span>
            </button>
            <ul class="dropdown-menu" style="margin: 1px; padding: 0;">
              <li><a href="<spring:url value="/account/payment/methods/edit/${encodedPaymentIds[status.index]}" />">Edit</a></li>
              <c:if test="${fn:length(paymentMethods) > 1}">
                <li style="margin-bottom: 10px;"><a href="<spring:url value="/account/payment/methods/remove/${encodedPaymentIds[status.index]}" />">Remove</a></li>
              </c:if>
            </ul>
          </div>
          <div>${creditCard.nameOnCreditCard}</div>
          <div>${creditCard.city}, ${creditCard.state} ${creditCard.zip}</div>
        </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <p>You have no credit cards saved</p>
    </c:otherwise>
  </c:choose>


  <div class="clear"></div>
  <c:if test="${sessionScope.CONTROLLING_USER.userId != -1}">
    <div class="buttons" style="margin-top: 10px 0 10px 0; padding: 10px 0 10px 0;">
      <a href="<spring:url value="/account/payment/methods/add" />" class="button action-m" style="float: right;"><span>Add New Card</span> </a>
    </div>
  </c:if>


</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>