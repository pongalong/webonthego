<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>

<script type="text/javascript" src="<spring:url value="/static/javascript/pages/accountActivity.js" />"></script>

</head>
<body class="app">
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>
  <%@ include file="/WEB-INF/views/include/header.jsp"%>

  <div class="container">
    <div id="main-content">
      <div class="span-18 colborder">
        <h3>Account Activity</h3>

        <div style="position: relative; margin-bottom: 30px;">
          <select id="deviceSelect" style="border-radius: 3px; border-color: #aaa;">
            <c:forEach var="acc" items="${accountList}">
              <option value="${acc.encodedAccountNum}">${acc.deviceInfo.label}</option>
            </c:forEach>
          </select>
          <div style="position: absolute; top: 0; bottom: 0; right: 0; margin: auto; height: 15px;">
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
          </div>
        </div>

        <c:set var="currentBalance" value="${accountDetail.account.balance}" />

        <c:choose>
          <c:when test="${not empty accountDetail.usageHistory.records}">
            <table>
              <tr>
                <th>Date and Time</th>
                <th>Type</th>
                <th style="text-align: right;">Usage</th>
                <th style="text-align: right;">Amount</th>
                <th style="text-align: right;">Balance</th>
                <th style="width: 16px;"></th>
              </tr>
              <c:forEach var="usageDetail" items="${accountDetail.usageHistory.currentPage}">
                <%@ include file="/WEB-INF/views/include/display/usageDetail.jsp"%>
              </c:forEach>
            </table>
            <c:set var="prevPageNum" value="${accountDetail.usageHistory.currentPageNum - 1}" />
            <c:set var="nextPageNum" value="${accountDetail.usageHistory.currentPageNum + 1}" />
            <c:if test="${prevPageNum > 0}">
              <span style="float: left"><a href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}/${prevPageNum}" />">&laquo;
                  Previous Page</a> </span>
            </c:if>
            <c:if test="${accountDetail.usageHistory.currentPageNum < accountDetail.usageHistory.pageCount}">
              <span style="float: right"><a href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}/${nextPageNum}" />">Next
                  Page &raquo;</a> </span>
            </c:if>
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

      </div>

      <div class="span-6 last sub-navigation">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

    </div>
    <!-- Close main-content -->
    <%@ include file="/WEB-INF/views/include/footer_links.jsp"%>
  </div>
  <!-- Close container -->

</body>
</html>