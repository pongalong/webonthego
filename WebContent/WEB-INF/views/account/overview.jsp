<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>

</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div class="mainbody">

      <div class="span-18 colborder" style="min-height: 200px;">

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Account Activity</h3>
        <c:choose>
          <c:when test="${not empty ACCOUNT_DETAILS}">
            <c:forEach var="accountDetail" items="${ACCOUNT_DETAILS}">
              <h4 style="float: left; display: inline-block; badding-bottom: 10px;">${accountDetail.deviceInfo.label}</h4>
              <c:choose>
                <c:when test="${not empty accountDetail.account.inactiveDate}">
                  <div class="badge" style="float: right;">Current Balance: Disconnected</div>
                </c:when>
                <c:otherwise>
                  <div class="badge" style="float: right;">
                    Current Balance: $
                    <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
                  </div>
                </c:otherwise>
              </c:choose>

              <c:choose>
                <c:when test="${not empty accountDetail.usageHistory.recordsSummary}">
                  <table>
                    <tr>
                      <th>Date and Time</th>
                      <th>Type</th>
                      <th>Usage</th>
                      <th style="text-align: right;">Amount</th>
                      <th style="width: 16px;"></th>
                    </tr>
                    <c:forEach var="usageDetail" items="${accountDetail.usageHistory.recordsSummary}" varStatus="status">
                      <%@ include file="/WEB-INF/views/include/display/usageDetail.jsp"%>
                    </c:forEach>
                  </table>
                  <div style="text-align: right; margin-bottom: 20px;">
                    <a href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}" />">View More &raquo;</a>
                  </div>
                </c:when>
                <c:otherwise>
                  <table>
                    <tr style="display: none;">
                      <td>No usage</td>
                    </tr>
                    <tr>
                      <td>No usage</td>
                    </tr>
                  </table>
                </c:otherwise>
              </c:choose>

              <div class="clear"></div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p>You have no devices with activity.</p>
          </c:otherwise>
        </c:choose>

        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Payments</h3>
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
            <div style="text-align: right;">
              <a href="<spring:url value="/account/payment/history" />">View More &raquo;</a>
            </div>
          </c:when>
          <c:otherwise>
            <p>You have no recent payments.</p>
          </c:otherwise>
        </c:choose>
        <div class="clear"></div>
      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>

    </div>
    <!-- Close mainbody -->

  </div>
  <!-- Close container -->

  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>

</body>
</html>