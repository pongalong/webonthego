<tr>
  <td>${paymentRecord.paymentDate.month}/${paymentRecord.paymentDate.day}/${paymentRecord.paymentDate.year} ${paymentRecord.paymentDate.hour}:<fmt:formatNumber
      value="${paymentRecord.paymentDate.minute}" pattern="00" /> <c:choose>
      <c:when test="${paymentRecord.paymentDate.hour >= 12}">pm</c:when>
      <c:otherwise>am</c:otherwise>
    </c:choose>
  </td>
  <td>${paymentRecord.paymentType}</td>
  <td>${paymentRecord.account}</td>
  <td style="text-align: right;">$<fmt:formatNumber value="${paymentRecord.paymentAmount}" pattern="0.00" /></td>
  <td style="text-align: right;"><c:choose>
      <c:when test="${not empty paymentRecord.refundDate}">
    refunded ${paymentRecord.refundDate}
    </c:when>
      <c:otherwise>

      </c:otherwise>
    </c:choose></td>
</tr>