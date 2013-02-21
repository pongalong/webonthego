<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <c:choose>
    <c:when test="${not empty ACCOUNT_DETAILS}">

      <form:form id="addCoupon" cssClass="validatedForm" method="post" commandName="coupon" cssStyle="overflow:hidden; height:300px;">
        <!-- Error Alert -->
        <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.coupon'].allErrors}">
          <div class="row clearfix">
            <div class="alert error">
              <h1>Please correct the following problems</h1>
              <form:errors path="couponCode" />
              <form:errors path="startDate" />
              <form:errors path="endDate" />
              <form:errors path="quantity" />
              <spring:bind path="coupon">
                <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
                  <span id="global.${status.index}.errors"><c:out value="${error}" /> </span>
                </c:forEach>
              </spring:bind>
            </div>
          </div>
        </c:if>

        <div class="slider" style="min-height: 140px;">
          <h3>Enter a Promotion Code</h3>

          <p>Enter your promotion code below, and click Next. Your discount will be applied to your account automatically. If your promotion code is for a
            discount on the monthly service fee, the discount will apply when that fee is charged. You will see your discount on the Account Overview and
            Activity page.</p>

          <div class="row clearfix">
            <form:label cssClass="required" path="couponCode">Coupon Code</form:label>
            <form:input cssClass="span-8 noSubmit" cssErrorClass="span-8 validationFailed noSubmit" path="couponCode" autocomplete="off" />
          </div>

          <div class="row clearfix pushed">
            <span id="couponMessage"></span>
          </div>

          <div class="buttons">
            <a href="#" class="mBtn continue" id="next_1">Next </a>
          </div>

        </div>

        <div class="slider" style="min-height: 139px; display: none;">
          <h3 style="margin-bottom: 10px;">Select the Device you want to apply the offer to</h3>

          <c:forEach var="accountDetail" items="${ACCOUNT_DETAILS}" varStatus="status">
            <div class="row clearfix">
              <input type="radio" name="account" value="${accountDetail.encodedAccountNum}" /> <span>${accountDetail.deviceInfo.label}</span>
            </div>
          </c:forEach>


          <div class="buttons">
            <a href="#" class="mBtn back">Back</a> <input type="submit" name="_eventId_submit" value="Apply" />
          </div>
        </div>

      </form:form>

    </c:when>
    <c:otherwise>
      <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Enter a Coupon Code</h3>
      <p style="min-height: 150px;">You have no devices to apply a coupon to.</p>
    </c:otherwise>
  </c:choose>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/pages/addCoupon.js' />"></script>
<%@ include file="/WEB-INF/views/include/footer.jsp"%>