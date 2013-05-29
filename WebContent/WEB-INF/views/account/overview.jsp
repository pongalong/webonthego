<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Account Activity</h3>
<c:choose>

  <c:when test="${not empty ACCOUNT_DETAILS}">

    <c:forEach var="ad" items="${ACCOUNT_DETAILS}">

      <div class="row">
        <div class="span10">
          <div class="overviewLabel clearfix">
            <!-- Device Label -->
            <h4>${ad.deviceInfo.label}</h4>

            <!-- Device Balance -->
            <div class="badge">
              Current Balance:
              <c:choose>
                <c:when test="${not empty ad.account.inactiveDate}">
                      Disconnected
                    </c:when>
                <c:otherwise>
                      $<fmt:formatNumber value="${ad.account.balance}" pattern="0.00" />
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <!-- Usage Summary -->
          <c:choose>
            <c:when test="${not empty ad.usageHistory.recordsSummary}">
              <table>
                <tr>
                  <th>Date and Time</th>
                  <th>Type</th>
                  <th style="text-align: right;">Usage</th>
                  <th style="text-align: right;">Amount</th>
                  <th style="width: 16px;"></th>
                </tr>
                <c:forEach var="usageDetail" items="${ad.usageHistory.recordsSummary}" varStatus="status">
                  <%@ include file="/WEB-INF/views/include/display/usageDetail.jsp"%>
                </c:forEach>
              </table>
              <a class="next" href="<spring:url value="/account/activity/${ad.encodedAccountNum}" />">View More</a>
              <div class="clear"></div>
            </c:when>
            <c:otherwise>
              <table>
                <tr class="hidden">
                  <td>No usage</td>
                </tr>
                <tr>
                  <td>No usage</td>
                </tr>
              </table>
            </c:otherwise>
          </c:choose>

        </div>
      </div>
    </c:forEach>

    <div class="clear"></div>

  </c:when>

  <c:otherwise>
    <p>You have no devices with activity.</p>
  </c:otherwise>

</c:choose>

<h3>Payments</h3>
<div class="row">
  <div class="span10">

    <c:choose>

      <c:when test="${not empty PAYMENT_HISTORY.newestRecord}">

        <table>
          <tr>
            <th>Date and Time</th>
            <th>Type</th>
            <th>Account</th>
            <th style="text-align: right;">Amount</th>
          </tr>
          <c:forEach var="paymentRecord" items="${PAYMENT_HISTORY.newestRecord}">
            <%@ include file="/WEB-INF/views/include/display/paymentRecord.jsp"%>
          </c:forEach>
        </table>
        <a class="next" href="<spring:url value="/account/payment/history" />">View More</a>

      </c:when>

      <c:otherwise>
        <p>You have no recent payments.</p>
      </c:otherwise>

    </c:choose>

  </div>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>