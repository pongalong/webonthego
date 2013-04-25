<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">

  <h3 style="margin-bottom: 10px; padding-bottom: 0px;">Payment History</h3>

  <c:choose>
    <c:when test="${not empty PAYMENT_HISTORY.records}">
      <table>
        <tr>
          <th>Date and Time</th>
          <th>Type</th>
          <th>Account</th>
          <th style="text-align: right;">Amount</th>
          <th style="text-align: right;"></th>
          <c:if test="${!empty sessionScope.controlling_user}">
              <th style="text-align: right;">Reverse</th>
          </c:if>
        </tr>
        <c:forEach var="paymentRecord" items="${PAYMENT_HISTORY.currentPage}">
          <%@ include file="/WEB-INF/views/include/display/paymentRecord_admin.jsp"%>
        </c:forEach>
      </table>
      <c:set var="prevPageNum" value="${PAYMENT_HISTORY.currentPageNum - 1}" />
      <c:set var="nextPageNum" value="${PAYMENT_HISTORY.currentPageNum + 1}" />
      <c:if test="${prevPageNum > 0}">
        <span style="float: left"><a href="<spring:url value="/account/payment/history/${prevPageNum}" />">&laquo; Previous Page</a> </span>
      </c:if>
      <c:if test="${PAYMENT_HISTORY.currentPageNum < PAYMENT_HISTORY.pageCount}">
        <span style="float: right"><a href="<spring:url value="/account/payment/history/${nextPageNum}" />">Next Page &raquo;</a> </span>
      </c:if>
    </c:when>
    <c:otherwise>
      <p>You have no recent payments.</p>
    </c:otherwise>
  </c:choose>

</div>

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>