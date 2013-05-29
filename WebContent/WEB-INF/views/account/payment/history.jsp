<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Payment History</h3>

<c:choose>
  <c:when test="${not empty PAYMENT_HISTORY.records}">
    <table>
      <tr>
        <th>Date and Time</th>
        <th>Type</th>
        <th>Account</th>
        <th style="text-align: right;">Amount</th>
        <c:if test="${not empty CONTROLLING_USER}">
          <sec:authorize ifAnyGranted="ROLE_ACCOUNTING_MANAGER, ROLE_SU">
            <th style="text-align: right;">Refund</th>
          </sec:authorize>
        </c:if>
      </tr>
      <c:forEach var="paymentRecord" items="${PAYMENT_HISTORY.currentPage}">
        <%@ include file="/WEB-INF/views/include/display/paymentRecord_admin.jsp"%>
      </c:forEach>
    </table>
    <c:set var="prevPageNum" value="${PAYMENT_HISTORY.currentPageNum - 1}" />
    <c:set var="nextPageNum" value="${PAYMENT_HISTORY.currentPageNum + 1}" />
    <c:if test="${prevPageNum > 0}">
      <a class="prev" href="<spring:url value="/account/payment/history/${prevPageNum}" />">Previous Page</a>
    </c:if>
    <c:if test="${PAYMENT_HISTORY.currentPageNum < PAYMENT_HISTORY.pageCount}">
      <a class="next" href="<spring:url value="/account/payment/history/${nextPageNum}" />">Next Page</a>
    </c:if>
  </c:when>
  <c:otherwise>
    <p>You have no recent payments.</p>
  </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>