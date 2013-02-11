<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder" style="min-height: 200px;">
  <h3>Account Activity</h3>


  <div style="position: relative; margin-bottom: 30px;">
    <select id="deviceSelect" style="border-radius: 3px; border-color: #aaa;">
      <c:forEach var="acc" items="${ACCOUNT_DETAILS}">
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
        <span style="float: right"><a href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}/${nextPageNum}" />">Next Page
            &raquo;</a> </span>
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

<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<script type="text/javascript" src="<spring:url value="/static/javascript/pages/accountActivity.js" />"></script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>