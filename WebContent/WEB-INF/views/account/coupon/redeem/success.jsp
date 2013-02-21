<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <h3>Coupon Applied!</h3>
  <p>The coupon was applied to your account. If the coupon you have received is a discount on the monthly access fee, the discount will be applied when the
    monthly access fee is charged.</p>
  <div>
    <b>${coupon.couponDetail.contract.description} for <c:choose>
        <c:when test="${coupon.couponDetail.contract.contractType > 0}">${coupon.couponDetail.duration} months</c:when>
        <c:when test="${coupon.couponDetail.contract.contractType < 0}">${coupon.couponDetail.amount} dollars</c:when>
        <c:otherwise>your account</c:otherwise>
      </c:choose>
    </b><br /> Device: ${accountDetail.deviceInfo.label}
  </div>

  <div class="buttons">
    <a href="<spring:url value="/" />" class="button action-m">Done </a>
  </div>
</div>

<div class="span-6 last sub-navigation formProgress">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript" src="<spring:url value='/static/javascript/pages/addCoupon.js' />"></script>
<%@ include file="/WEB-INF/views/include/footer.jsp"%>