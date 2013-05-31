<tr>
  <td>${paymentRecord.paymentDate.month}/${paymentRecord.paymentDate.day}/${paymentRecord.paymentDate.year} ${paymentRecord.paymentDate.hour}:<fmt:formatNumber
      value="${paymentRecord.paymentDate.minute}" pattern="00" />
  </td>
  <td>${paymentRecord.paymentType}</td>
  <td>${paymentRecord.account}</td>
  <td style="text-align: right;">$<fmt:formatNumber value="${paymentRecord.paymentAmount}" pattern="0.00" /></td>

  <c:if test="${not empty CONTROLLING_USER}">
    <sec:authorize ifAnyGranted="ROLE_ACCOUNTING_MANAGER, ROLE_SU">
      <td style="text-align: right;"><c:if test="${not empty CONTROLLING_USER}">
          <c:choose>
            <c:when test="${empty paymentRecord.refundDate}">
              <a href="<spring:url value="/admin/refund/${paymentRecord.transId}" />">refund</a>
            </c:when>
            <c:otherwise>
              <span style="color: balck; text-align: center; padding-left: 20px; padding-righ: 0px">
                ${paymentRecord.refundDate.month}/${paymentRecord.refundDate.day}/${paymentRecord.refundDate.year} ${paymentRecord.refundDate.hour}:<fmt:formatNumber
                  value="${paymentRecord.refundDate.minute}" pattern="00" />
              </span>
            </c:otherwise>
          </c:choose>
        </c:if></td>
    </sec:authorize>
  </c:if>

</tr>