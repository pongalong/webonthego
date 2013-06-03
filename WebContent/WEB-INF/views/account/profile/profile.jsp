<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<c:if test="${not empty param.notification_sent}">
  <div class="alert alert-info">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <h4>An email has been sent to you</h4>
    An email has been sent to ${param.notification_sent} with further instructions
  </div>
</c:if>

<c:if test="${param.updated == 'password'}">
  <div class="alert alert-success">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <h4>Profile Updated</h4>
    Your password has been successfully updated!
  </div>
</c:if>

<c:if test="${param.updated == 'email'}">
  <div class="alert alert-success">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <h4>Profile Updated</h4>
    Your email has been successfully updated!
  </div>
</c:if>

<h3>Login Information</h3>

<div>

  <sec:authorize ifAnyGranted="ROLE_SU, ROLE_ADMIN, ROLE_MANAGER">
    <div class="row" style="margin: 10px 0;">
      <div class="span2">Status</div>
      <c:choose>
        <c:when test="${USER.enabled}">
          <div class="span4">Enabled</div>
          <div class="span2">
            <a href="#disableUser" role="button" class="btn" data-toggle="modal">Disable User</a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="span4">Disabled</div>
          <div class="span2">
            <a href="#enableUser" role="button" class="btn" data-toggle="modal">Enable User</a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>

    <!-- Modal -->
    <div id="enableUser" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="enableUserLabel" aria-hidden="true">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="enableUserLabel">Enable user ${USER.email}</h3>
      </div>
      <div class="modal-body">
        <p>Are you sure you want to enable user ${USER.email}? This will allow the user to login with his given credentials. Their device is unaffected.</p>
      </div>
      <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
        <button class="btn btn-primary" onclick="location.href='<spring:url value="/profile/user/enable"/>'">Enable</button>
      </div>
    </div>

    <!-- Modal -->
    <div id="disableUser" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="disableUserLabel" aria-hidden="true">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="disableUserLabel">Disable user ${USER.email}</h3>
      </div>
      <div class="modal-body">
        <p>Are you sure you want to disable user ${USER.email}? This will prevent the user from logging in. Their device is unaffected.</p>
      </div>
      <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
        <button class="btn btn-primary" onclick="location.href='<spring:url value="/profile/user/disable"/>'">Disable</button>
      </div>
    </div>


  </sec:authorize>

  <div class="row" style="margin: 10px 0;">
    <div class="span2">E-Mail Address:</div>
    <div class="span4">${USER.email}</div>
    <div class="span1">
      <a href="<spring:url value="/profile/update/email"/>">Change</a>
    </div>
  </div>

  <div class="row" style="margin: 10px 0;">
    <div class="span2">Password:</div>
    <div class="span4">&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</div>
    <div class="span1">
      <a href="<spring:url value="/profile/update/password"/>">Change</a>
    </div>
  </div>

</div>

<div class="clear"></div>


<h3>Credit Cards</h3>

<div id="paymentMethodContainer">
  <c:choose>
    <c:when test="${not empty PAYMENT_METHODS}">
      <c:forEach var="creditCard" items="${PAYMENT_METHODS}" varStatus="status">
        <div class="address" style="margin-bottom: 30px;">
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
            <ul class="dropdown-menu" style="margin: 1px; padding: 0; width: 180px;">
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
</div>

<div class="clear"></div>
<c:if test="${sessionScope.CONTROLLING_USER.userId != -1}">
  <div class="buttons" style="margin-top: 10px 0 10px 0; padding: 10px 0 10px 0;">
    <a href="<spring:url value="/account/payment/methods/add" />" class="button action-m" style="float: right;"><span>Add New Card</span> </a>
  </div>
</c:if>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>