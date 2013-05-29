<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Promotion Coupon Applied!</h3>
<p>A promotional coupon was applied to your account:</p>
<div>
  <b>${coupon.couponDetail.contract.description} for <c:choose>
      <c:when test="${coupon.couponDetail.contract.contractType > 0}">${coupon.couponDetail.duration} months</c:when>
      <c:when test="${coupon.couponDetail.contract.contractType < 0}">&#36;${coupon.couponDetail.amount} dollars</c:when>
      <c:otherwise>your account</c:otherwise>
    </c:choose>
  </b><br /> Device: ${accountDetail.deviceInfo.label}
</div>

<div class="buttons">
  <a href="<spring:url value="/" />" class="button action-m">Done</a>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>