<tr>
  <td>${paymentRecord.paymentDate.month}/${paymentRecord.paymentDate.day}/${paymentRecord.paymentDate.year} ${paymentRecord.paymentDate.hour}:<fmt:formatNumber
      value="${paymentRecord.paymentDate.minute}" pattern="00" />>
  </td>
  <td>${paymentRecord.paymentType}</td>
  <td>${paymentRecord.account}</td>
  <td style="text-align: right;">$<fmt:formatNumber value="${paymentRecord.paymentAmount}" pattern="0.00" /></td>
</tr>