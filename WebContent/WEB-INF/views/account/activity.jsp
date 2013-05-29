<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Account Activity</h3>

<!-- Begin Device Select and Balance -->
<div class="row">
  <div class="span8">
    <select id="deviceSelect">
      <c:forEach var="acc" items="${ACCOUNT_DETAILS}">
        <option value="${acc.encodedAccountNum}">${acc.deviceInfo.label}</option>
      </c:forEach>
    </select>
  </div>
  <div class="span2">
    <div class="badge" style="float: right;">
      Current Balance:
      <c:choose>
        <c:when test="${not empty accountDetail.account.inactiveDate}">
       Disconnected
      </c:when>
        <c:otherwise>
          <fmt:formatNumber value="${accountDetail.account.balance}" pattern="0.00" />
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
<!-- End Device Select and Balance -->

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
      <a class="prev" href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}/${prevPageNum}" />">Previous Page</a>
    </c:if>
    <c:if test="${accountDetail.usageHistory.currentPageNum < accountDetail.usageHistory.pageCount}">
      <a class="next" href="<spring:url value="/account/activity/${accountDetail.encodedAccountNum}/${nextPageNum}" />">Next Page</a>
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

<script type="text/javascript">
	$(function() {
		$("#deviceSelect").change(function() {
			$("#curtain").fadeIn("fast").center().height($(document).height());
			$("#centerPopup").fadeIn("fast").center();
			var location = '/account/activity/' + $("#deviceSelect option:selected").val();
			window.location.href = location;
		});
		var deviceId = window.location.href.substring(0, window.location.href.lastIndexOf("/"));
		deviceId = deviceId.substring(deviceId.lastIndexOf("/") + 1);
		$("#deviceSelect").val(deviceId);
	});
</script>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>