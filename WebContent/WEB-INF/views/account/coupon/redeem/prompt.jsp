<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<c:choose>

  <c:when test="${empty ACCOUNT_DETAILS}">
    <h3>Enter a Coupon Code</h3>
    <p>You have no devices to apply a coupon to.</p>
  </c:when>

  <c:otherwise>


    <!-- Begin Form -->
    <form:form id="addCoupon" cssClass="form-horizontal" method="post" commandName="couponRequest">
      <fieldset>
        <!-- Form Title -->
        <legend>Enter a Promotion Code</legend>

        <!-- Error Alert -->
        <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.couponRequest'].allErrors}">
          <div class="alert alert-error">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4>Please correct the following problems</h4>
            <form:errors path="account.accountNo" />
            <form:errors path="coupon.couponCode" />
            <form:errors path="coupon.startDate" />
            <form:errors path="coupon.endDate" />
            <form:errors path="coupon.quantity" />
            <spring:bind path="couponRequest">
              <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
              </c:forEach>
            </spring:bind>
          </div>
        </c:if>

        <!-- Form Description -->
        <p class="well">Enter your promotion code below, and click Next. Your discount will be applied to your account automatically. If your promotion code is for a
          discount on the monthly service fee, the discount will apply when that fee is charged. You will see your discount on the Account Overview and Activity
          page.</p>

        <!-- Account Selection -->
        <div class="control-group">
          <form:label path="account.accountNo" cssClass="control-label required">Select a Device</form:label>
          <div class="controls">
            <form:select path="account.accountNo" cssClass="span4" cssErrorClass="span4 validationFailed">
              <form:option value="0">Select a Device...</form:option>
              <form:options items="${ACCOUNT_DETAILS}" itemLabel="deviceInfo.label" itemValue="deviceInfo.accountNo" />
            </form:select>
          </div>
        </div>

        <!-- Coupon Code Entry -->
        <div class="control-group">
          <form:label path="coupon.couponCode" cssClass="control-label required">Coupon Code</form:label>
          <div class="controls">
            <form:input path="coupon.couponCode" cssClass="span4" cssErrorClass="span4 validationFailed" autocomplete="off" />
          </div>
        </div>

        <!-- Coupon AJAX Message (unused) -->
        <div class="row">
          <span id="couponMessage"></span>
        </div>

        <!-- Form Buttons -->
        <div class="controls">
          <button type="submit" name="_eventId_submit" class="button">Apply Promotion</button>
        </div>
      </fieldset>
    </form:form>
    <!-- End Form -->

  </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>