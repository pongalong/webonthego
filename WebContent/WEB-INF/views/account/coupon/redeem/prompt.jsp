<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <c:choose>
    <c:when test="${empty ACCOUNT_DETAILS}">
      <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Enter a Coupon Code</h3>
      <p style="min-height: 150px;">You have no devices to apply a coupon to.</p>
    </c:when>
    <c:otherwise>
      <!-- Begin Form -->
      <form:form id="addCoupon" cssClass="validatedForm" method="post" commandName="couponRequest">
        <!-- Form Title -->
        <h3>Enter a Promotion Code</h3>
        <!-- Error Alert -->
        <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.couponRequest'].allErrors}">
          <div class="alert error">
            <h1>Please correct the following problems</h1>
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
        <p>Enter your promotion code below, and click Next. Your discount will be applied to your account automatically. If your promotion code is for a
          discount on the monthly service fee, the discount will apply when that fee is charged. You will see your discount on the Account Overview and Activity
          page.</p>

        <!-- Account Selection -->
        <div class="row clearfix">
          <form:label path="account.accountNo">Select a Device</form:label>
          <form:select path="account.accountNo" cssClass="required" cssErrorClass="validationFailed">
            <option value="0">Select a Device...</option>
            <form:options items="${ACCOUNT_DETAILS}" itemLabel="deviceInfo.label" itemValue="deviceInfo.accountNo" cssErrorClass="validationFailed" />
          </form:select>
        </div>

        <!-- Coupon Code Entry -->
        <div class="row clearfix">
          <form:label cssClass="required" path="coupon.couponCode">Coupon Code</form:label>
          <form:input cssErrorClass="validationFailed" path="coupon.couponCode" autocomplete="off" />
        </div>

        <!-- Coupon AJAX Message (unused) -->
        <div class="row clearfix pushed">
          <span id="couponMessage"></span>
        </div>

        <!-- Form Buttons -->
        <div class="buttons">
          <input type="submit" name="_eventId_submit" value="Apply Promotion" />
        </div>

      </form:form>
      <!-- End Form -->

    </c:otherwise>
  </c:choose>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/pages/addCoupon.js' />"></script>
<%@ include file="/WEB-INF/views/include/footer.jsp"%>